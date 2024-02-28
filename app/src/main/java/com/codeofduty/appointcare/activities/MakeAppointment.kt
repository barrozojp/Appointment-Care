package com.codeofduty.appointcare.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.codeofduty.appointcare.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MakeAppointment : Fragment() {


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
