package com.codeofduty.appointcare.activities

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.ApiService
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.models.MyBookings
import com.codeofduty.appointcare.models.UpdateBookingStatusRequest
import com.codeofduty.appointcare.models.UpdateSympConsPres
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConsultationFragment : Fragment() {

    private lateinit var apiService: ApiService


    companion object {
        private const val ARG_BOOKING_ID = "booking_id"
        private const val ARG_PATIENT_NAME = "patient_name"
        private const val ARG_PATIENT_EMAIL = "patient_email"
        private const val ARG_PATIENT_NUMBER = "patient_number"

        fun newInstance(bookingId: String, patientName: String, patientEmail: String, patientNumber: String): ConsultationFragment {
            val fragment = ConsultationFragment()
            val args = Bundle()
            args.putString(ARG_BOOKING_ID, bookingId)
            args.putString(ARG_PATIENT_NAME, patientName)
            args.putString(ARG_PATIENT_EMAIL, patientEmail)
            args.putString(ARG_PATIENT_NUMBER, patientNumber)
            fragment.arguments = args
            return fragment
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_consultation, container, false)

        // Retrieve arguments
        val patientName = arguments?.getString(ARG_PATIENT_NAME)
        val patientEmail = arguments?.getString(ARG_PATIENT_EMAIL)
        val patientNumber = arguments?.getString(ARG_PATIENT_NUMBER)
        val bookingId = arguments?.getString(ARG_BOOKING_ID)

        // Set patient details in the layout
        view.findViewById<TextView>(R.id.patientName).text = patientName
        view.findViewById<TextView>(R.id.emailPatient).text = patientEmail
        view.findViewById<TextView>(R.id.numPatient).text = patientNumber

        // Initialize ApiService
        apiService = RetrofitClient.getService()

        // Find the button
        val postConsultationButton = view.findViewById<Button>(R.id.PostConsultation)

        // Set click listener for the button
        postConsultationButton.setOnClickListener {
            // Display loading dialog
            val loadingDialog = showLoadingDialog(requireContext())

            // Call the method to process the consultation
            processConsultation()

            // Dismiss the loading dialog after consultation processing is complete
            loadingDialog.dismiss()
        }

        return view
    }
    private fun showLoadingDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        return dialog
    }

    private fun processConsultation() {
        // Assuming you have references to your EditTexts and CheckBoxes
        val observation = view?.findViewById<EditText>(R.id.observationEditText)?.text.toString()
        val prescription = view?.findViewById<EditText>(R.id.PrescriptionEditText)?.text.toString()
        val symptoms = mutableListOf<String>()

        if (observation.isBlank() || prescription.isBlank()) {
            showToast("Observation and prescription cannot be empty")
            return
        }

        // Add selected symptoms to the list
        val cough = view?.findViewById<CheckBox>(R.id.cough)
        val painInBone = view?.findViewById<CheckBox>(R.id.painInBone)
        val tiredness = view?.findViewById<CheckBox>(R.id.tiredness)
        val unexplainedWeightLoss = view?.findViewById<CheckBox>(R.id.unexplainedWeightLoss)
        val paleness = view?.findViewById<CheckBox>(R.id.paleness)
        val unexplainedFever = view?.findViewById<CheckBox>(R.id.unexplainedFever)
        val lumpsOfSwelling = view?.findViewById<CheckBox>(R.id.lumpsOfSwelling)
        val drenchingNightSweats = view?.findViewById<CheckBox>(R.id.drenchingNightSweats)
        val shortnessOfBreath = view?.findViewById<CheckBox>(R.id.shortnessOfBreath)
        val unexplainedRash = view?.findViewById<CheckBox>(R.id.unexplainedRash)
        val frequentInfection = view?.findViewById<CheckBox>(R.id.frequentInfection)
        val bruising = view?.findViewById<CheckBox>(R.id.bruising)

        if (cough != null) {
            if (cough.isChecked) symptoms.add("cough")
        }
        if (painInBone != null) {
            if (painInBone.isChecked) symptoms.add("painInBone")
        }
        if (tiredness != null) {
            if (tiredness.isChecked) symptoms.add("tiredness")
        }
        if (unexplainedWeightLoss != null) {
            if (unexplainedWeightLoss.isChecked) symptoms.add("unexplainedWeightLoss")
        }
        if (paleness != null) {
            if (paleness.isChecked) symptoms.add("paleness")
        }
        if (unexplainedFever != null) {
            if (unexplainedFever.isChecked) symptoms.add("unexplainedFever")
        }
        if (lumpsOfSwelling != null) {
            if (lumpsOfSwelling.isChecked) symptoms.add("lumpsOfSwelling")
        }
        if (drenchingNightSweats != null) {
            if (drenchingNightSweats.isChecked) symptoms.add("drenchingNightSweats")
        }
        if (shortnessOfBreath != null) {
            if (shortnessOfBreath.isChecked) symptoms.add("shortnessOfBreath")
        }
        if (unexplainedRash != null) {
            if (unexplainedRash.isChecked) symptoms.add("unexplainedRash")
        }
        if (frequentInfection != null) {
            if (frequentInfection.isChecked) symptoms.add("frequentInfection")
        }
        if (bruising != null) {
            if (bruising.isChecked) symptoms.add("bruising")
        }


        // Call the method to update consultation details
        val bookingId = arguments?.getString(ARG_BOOKING_ID)
        if (bookingId != null) {
            updateConsultation(observation, prescription, symptoms.toList())

            // After updating the consultation, update the booking status to "Done"
            updateBookingStatus(bookingId)
        } else {
            showToast("Booking ID not found")
        }
    }

    // Function to update consultation details
    private fun updateConsultation(observation: String, prescription: String, symptoms: List<String>) {
        val bookingId = arguments?.getString(ARG_BOOKING_ID)

        if (bookingId != null) {
            val loadingDialog = showLoadingDialog(requireContext())

            val updateData = UpdateSympConsPres(observation, prescription, symptoms)
            apiService.addSymptomsObsAndPres(bookingId, updateData).enqueue(object : Callback<MyBookings> {
                override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                    loadingDialog.dismiss()
                    if (response.isSuccessful) {
                        showToast("Consultation updated successfully")

                        // Navigate to HomeFragment
                        val fragmentManager = requireActivity().supportFragmentManager
                        fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, HomeFragment())
                            .addToBackStack(null)
                            .commit()
                    } else {
                        showToast("Failed to update consultation")
                    }
                }

                override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                    loadingDialog.dismiss()
                    showToast("Failed to update consultation: ${t.message}")
                }
            })
        } else {
            showToast("Booking ID not found")
        }
    }
    // Function to update booking status
    private fun updateBookingStatus(bookingId: String) {
        val requestBody = UpdateBookingStatusRequest("Done") // Set the status to "Done"

        apiService.updateBookingStatus(bookingId, requestBody).enqueue(object : Callback<MyBookings> {
            override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                // Handle response
                if (response.isSuccessful) {
                    showToast("Booking status updated successfully to Done")
                } else {
                    showToast("Failed to update booking status")
                }
            }

            override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                // Handle failure
                showToast("Failed to update booking status: ${t.message}")
            }
        })
    }







    // Function to show toast message
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }



}
