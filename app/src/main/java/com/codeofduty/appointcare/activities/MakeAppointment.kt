package com.codeofduty.appointcare.activities

import DoctorUsers
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.models.BookAppointment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MakeAppointment : Fragment() {

    companion object {
        private const val ARG_DOCTOR_ID = "doctorId"

        fun newInstance(doctorId: String): MakeAppointment {
            val fragment = MakeAppointment()
            val args = Bundle()
            args.putString(ARG_DOCTOR_ID, doctorId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var doctorId: String

    private lateinit var NameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var NumberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doctorId = it.getString(ARG_DOCTOR_ID, "")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve user data from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val fname = sharedPreferences.getString("fname", null)
        val lname = sharedPreferences.getString("lname", null)
        val number = sharedPreferences.getString("number", null)
        val email = sharedPreferences.getString("email", null)

        // Concatenate first name and last name
        val fullName = "$fname $lname"

        // Set the text of EditText fields with the retrieved user data
        NameEditText.setText(fullName)
        NumberEditText.setText(number)
        emailEditText.setText(email)

        // Retrieve the doctor's ID passed from the previous fragment
        val doctorId = arguments?.getString(ARG_DOCTOR_ID)
        // Fetch the doctor's details from the API using the doctorId
        fetchDoctorDetails(doctorId)

        // Find the "Book Appointment" button
        val bookAppointmentBtn = view.findViewById<Button>(R.id.bookAppoint_btn)

        // Set an OnClickListener for the "Book Appointment" button
        bookAppointmentBtn.setOnClickListener {
            // Call the bookAppointment function when the button is clicked
            bookAppointment()
        }
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

        // Initialize EditText fields
        NameEditText = view.findViewById(R.id.NameEditText);
        NumberEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);


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

        // Format the time in 24-hour format
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(calendar.time)
    }

    private fun bookAppointment() {
        // Retrieve user data from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("_id", null)

        // Check if the fragment's view is null
        if (view == null) {
            Toast.makeText(requireContext(), "Fragment view is not available", Toast.LENGTH_SHORT).show()
            return
        }

        // Retrieve input values from EditText fields
        val fullName = NameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val number = NumberEditText.text.toString().trim()
        val date = requireView().findViewById<EditText>(R.id.dateEditText)?.text.toString().trim()
        val time = requireView().findViewById<EditText>(R.id.timeEditText)?.text.toString().trim()

        // Check the state of checkboxes
        val onlineCheckBox = view?.findViewById<CheckBox>(R.id.checkbox_online)
        val f2fCheckBox = view?.findViewById<CheckBox>(R.id.checkbox_f2f)

        // Determine the value of the online parameter based on checkbox states
        val online = if (onlineCheckBox?.isChecked == true) "true" else "false"
        val f2f = if (f2fCheckBox?.isChecked == true) "true" else "false"


        // Check if any of the fields are empty
        if (fullName.isEmpty() || email.isEmpty() || number.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Construct the appointment object
        val appointment = BookAppointment(
            doctorId = doctorId,
            fullName = fullName,
            email = email,
            number = number,
            online = online,
            f2f = f2f,
            date = date,
            time = time
        )

        // Call the bookAppointment function, passing the BookAppointment object and the userId
        val call: Call<BookAppointment> = RetrofitClient.getService().bookAppointment(userId!!, appointment)
        call.enqueue(object : Callback<BookAppointment> {
            override fun onResponse(call: Call<BookAppointment>, response: Response<BookAppointment>) {
                if (response.isSuccessful) {
                    // Handle successful appointment booking
                    Toast.makeText(requireContext(), "Appointment booked successfully", Toast.LENGTH_SHORT).show()
                    // Optionally, you can navigate to a different fragment or perform any other action
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to book appointment. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookAppointment>, t: Throwable) {
                // Handle failure
                Toast.makeText(requireContext(), "Failed to connect to server. Please try again later.", Toast.LENGTH_SHORT).show()
            }
        })
    }


}
