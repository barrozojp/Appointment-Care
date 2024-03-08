package com.codeofduty.appointcare.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.codeofduty.appointcare.models.EditSchedule
import com.codeofduty.appointcare.models.MyBookings
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditScheduleFragment : Fragment() {

    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_schedule, container, false)

        apiService = RetrofitClient.getService()

        // Retrieve booking ID from arguments bundle
        val bookingId = arguments?.getString("bookingId")

        // Use the booking ID as needed
        if (bookingId != null) {
            // Display the booking ID in a TextView
            view?.findViewById<TextView>(R.id.bookingIdTextView)?.text =
                "Booking ID: $bookingId"
        }

        val dateEditText = view.findViewById<EditText>(R.id.dateEditText)
        val timeEditText = view.findViewById<EditText>(R.id.timeEditText)

        dateEditText.setOnClickListener {
            showDatePicker()
        }

        timeEditText.setOnClickListener {
            showTimePicker()
        }

        view.findViewById<Button>(R.id.resched_btn).setOnClickListener {
            val bookingId = arguments?.getString("bookingId")
            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()
            val loadingDialog = createLoadingDialog()

            // Check if date and time fields are empty
            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please select date and time", Toast.LENGTH_SHORT).show()
            } else {
                if (bookingId != null) {
                    // Create an EditSchedule object
                    val editSchedule = EditSchedule(date, isOnline(), "Request", time)

                    // Call the API to reschedule the booking
                    rescheduleBooking(bookingId, editSchedule, loadingDialog)
                } else {
                    // Handle the case where booking ID is null
                    Toast.makeText(requireContext(), "Booking ID is null", Toast.LENGTH_SHORT).show()
                }
            }
        }

        view.findViewById<Button>(R.id.cancel_btn).setOnClickListener {
            // Use fragmentManager to pop the current fragment from the back stack
            fragmentManager?.popBackStack()
        }


        return view
    }

    private fun isOnline(): Boolean {
        val onlineCheckBox = view?.findViewById<CheckBox>(R.id.checkbox_online)
        return onlineCheckBox?.isChecked ?: false
    }

    private fun rescheduleBooking(bookingId: String, editSchedule: EditSchedule, loadingDialog: AlertDialog) {
        loadingDialog.show()

        apiService.rescheduleBooking(bookingId, editSchedule).enqueue(object :
            Callback<MyBookings> {
            override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                loadingDialog.dismiss() // Dismiss loading dialog on response
                if (response.isSuccessful) {
                    // Handle successful response
                    Toast.makeText(
                        requireContext(),
                        "Booking rescheduled successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    fragmentManager?.popBackStack()
                } else {
                    // Handle unsuccessful response
                    val errorMessage = response.errorBody()?.string()
                    Toast.makeText(
                        requireContext(),
                        "Failed to reschedule booking: $errorMessage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                loadingDialog.dismiss() // Dismiss loading dialog on failure
                // Handle failure
                Toast.makeText(
                    requireContext(),
                    "Failed to reschedule booking: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
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
                val formattedMonth =
                    if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"
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

    private fun createLoadingDialog(): AlertDialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false) // Prevent dialog dismissal on outside touch or back press

        val alertDialog = builder.create()

        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_loading_background)


        return alertDialog


    }

}