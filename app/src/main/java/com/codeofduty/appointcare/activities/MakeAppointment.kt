package com.codeofduty.appointcare.activities

import DoctorUsers
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
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
import com.codeofduty.appointcare.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MakeAppointment : Fragment() {

    companion object {
        private const val ARG_DOCTOR_ID = "_id"

        fun newInstance(_id: String): MakeAppointment {
            val fragment = MakeAppointment()
            val args = Bundle()
            args.putString(ARG_DOCTOR_ID, _id)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var _id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _id = it.getString(ARG_DOCTOR_ID, "")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the doctor's ID passed from the previous fragment
        val _id = arguments?.getString(ARG_DOCTOR_ID)
        // Fetch the doctor's details from the API using the doctorId
        fetchDoctorDetails(_id)
    }


    private fun fetchDoctorDetails(doctorId: String?) {
        val service = RetrofitClient.getService()
        val call = service.appointDoctorDetails(doctorId ?: "")

        call.enqueue(object : Callback<DoctorUsers> {
            override fun onResponse(call: Call<DoctorUsers>, response: Response<DoctorUsers>) {
                if (response.isSuccessful) {
                    val doctorDetails = response.body()
                    if (doctorDetails != null) {
                        // Update the UI with the doctor's details
                        updateDoctorDetailsUI(doctorDetails)
                    } else {
                        // Handle empty response (no doctor details found)
                        Toast.makeText(requireContext(), "No doctor details found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to fetch doctor details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DoctorUsers>, t: Throwable) {
                // Handle failure
                Toast.makeText(requireContext(), "Error fetching doctor details", Toast.LENGTH_SHORT).show()
            }
        })
    }





    private fun updateDoctorDetailsUI(doctorDetails: DoctorUsers?) {
        // Check if the doctor's details and the fragment view are not null
        if (doctorDetails != null && view != null) {
            // Find the views in the fragment's layout
            val tvNameUser = requireView().findViewById<TextView>(R.id.tv_nameUser)
            val tvSpecialty = requireView().findViewById<TextView>(R.id.tv_Specialty)
            val tvmd = requireView().findViewById<TextView>(R.id.tv_mdYEAR)
            val f2fCheckBox = requireView().findViewById<CheckBox>(R.id.checkbox_f2f)
            val onlineCheckBox = requireView().findViewById<CheckBox>(R.id.checkbox_online)


            // Set the doctor's details to the respective views
            tvNameUser?.text = "${doctorDetails.Fname} ${doctorDetails.Lname}"
            tvSpecialty?.text = "${doctorDetails.specialty}"
            tvmd?.text = "MD since ${doctorDetails.md}"

            // Set visibility of checkboxes based on doctor's availability
            f2fCheckBox?.visibility = if (doctorDetails.f2f == false) View.VISIBLE else View.GONE
            onlineCheckBox?.visibility = if (doctorDetails.online == false) View.VISIBLE else View.GONE
        }
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_make_appointment, container, false)

        // Find the date EditText field
        val dateEditText = view.findViewById<EditText>(R.id.dateEditText)

        // Request focus on the EditText
        dateEditText.requestFocus()

        // Set an OnClickListener to show the date picker dialog
        dateEditText.setOnClickListener {
            showDatePicker()
        }
        // Find the time EditText field
        val timeEditText = view.findViewById<EditText>(R.id.timeEditText)

        // Request focus on the EditText
        timeEditText.requestFocus()

        // Set an OnClickListener to show the time picker dialog
        timeEditText.setOnClickListener {
            showTimePicker()
        }

        // Find the "Cancel" button
        val cancelBtn = view.findViewById<Button>(R.id.cancel_btn)

        // Set an OnClickListener for the "Cancel" button
        cancelBtn.setOnClickListener {
            // Navigate back to the previous fragment (SearchFragment)
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val formattedDay = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val formattedMonth = if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
                val selectedDate = "$formattedDay/$formattedMonth/$year"
                // Set the selected date in the EditText field
                view?.findViewById<EditText>(R.id.dateEditText)?.setText(selectedDate)
            },
            year, month, day
        )

        // Show the date picker dialog
        datePickerDialog.show()
    }
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedTime = formatTime(hourOfDay, minute)
                // Set the selected time in the EditText field
                view?.findViewById<EditText>(R.id.timeEditText)?.setText(selectedTime)
            },
            hour, minute, false // Use false to indicate 12-hour format
        )

        // Show the time picker dialog
        timePickerDialog.show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        // Format the time in 12-hour format with AM/PM
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return timeFormat.format(calendar.time)
    }
}
