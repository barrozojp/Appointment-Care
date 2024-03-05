package com.codeofduty.appointcare.activities

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.ApiService
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.models.MyBookings
import com.codeofduty.appointcare.models.UpdateSympConsPres
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ConsultationFragment : Fragment() {

    private lateinit var apiService: ApiService
    private var user: UserX? = null // Property to store user data


    companion object {
        private const val ARG_PATIENT_ID = "patient_id"

        fun newInstance(patientId: String): ConsultationFragment {
            val fragment = ConsultationFragment()
            val args = Bundle()
            args.putString(ARG_PATIENT_ID, patientId)
            fragment.arguments = args
            return fragment
        }
    }

    private var patientId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            patientId = it.getString(ARG_PATIENT_ID)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_consultation, container, false)

        // Initialize ApiService
        apiService = RetrofitClient.getService()

        // Retrieve user data
        user = getUserData()

        // Find the button
        val postConsultationButton = view.findViewById<Button>(R.id.PostConsultation)

        // Set click listener for the button
        postConsultationButton.setOnClickListener {
            // Call the method to process the consultation
            processConsultation()
        }

        return view
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
        updateConsultation(observation, prescription, symptoms.toList())
    }

    // Function to update consultation details
    private fun updateConsultation(observation: String, prescription: String, symptoms: List<String>) {
        val userId = user?._id
        val patientId = arguments?.getString(ARG_PATIENT_ID)

        if (userId != null && patientId != null) {
            if (!::apiService.isInitialized) {
                apiService = RetrofitClient.getService()
            }

            val updateData = UpdateSympConsPres(observation, patientId, prescription, symptoms)
            apiService.addSymptomsObsAndPres(userId, updateData).enqueue(object : Callback<MyBookings> {
                override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                    if (response.isSuccessful) {
                        showToast("Consultation updated successfully")
                    } else {
                        showToast("Failed to update consultation")
                    }
                }

                override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                    showToast("Failed to update consultation: ${t.message}")
                }
            })
        } else {
            showToast("Patient ID not found or user data not available")
        }
    }




    // Function to show toast message
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    // Function to get user data from SharedPreferences
    private fun getUserData(): UserX? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val _id = sharedPreferences.getString("_id", null)
        return if (_id != null) {
            UserX(_id = _id)
        } else {
            null
        }
    }

}
