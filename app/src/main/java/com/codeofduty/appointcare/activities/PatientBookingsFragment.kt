package com.codeofduty.appointcare.activities

import DoctorUsers
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.ApiService
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.models.MyBookings
import com.codeofduty.appointcare.models.Schedule
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class PatientBookingsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PatientBookingsAdapter
    private var mListBookings = ArrayList<PatientBookingsData>()
    private lateinit var apiService: ApiService
    private lateinit var loadingCARD: CardView
    private lateinit var noAppointsCARD: CardView
    private lateinit var tv_myAppointments: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_patient_bookings, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMyBookings)
        loadingCARD = view.findViewById(R.id.loadingCARD)
        tv_myAppointments = view.findViewById(R.id.tv_myAppointments)
        noAppointsCARD = view.findViewById(R.id.noAppointsCARD)

        // Initialize the ApiService
        apiService = RetrofitClient.getService()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = PatientBookingsAdapter(mListBookings, apiService, childFragmentManager, object : OnRescheduleClickListener {
            override fun onRescheduleClick(bookingId: String) {
                // Pass the booking ID to the EditScheduleFragment
                val fragment = EditScheduleFragment().apply {
                    arguments = Bundle().apply {
                        putString("bookingId", bookingId)
                    }
                }
                val transaction = fragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.replace(R.id.fragment_container, fragment)
                }
                if (transaction != null) {
                    transaction.addToBackStack(null)
                }
                if (transaction != null) {
                    transaction.commit()
                }
            }
        })




        recyclerView.adapter = adapter

        // Initialize the ApiService
        apiService = RetrofitClient.getService()

        fetchData()

        return view
    }

    private fun fetchData() {
        val user = getUserData()
        user?.let { userData ->
            val userId = userData._id
            userId?.let { _id ->
                apiService.getBookings(_id).enqueue(object : Callback<MyBookings> {
                    override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                        if (response.isSuccessful) {
                            val myBookings = response.body()

                            myBookings?.let { bookings ->
                                var hasSpecificStatusBookings = false // Flag to track if any bookings have the specified statuses
                                for (schedule in bookings.schedules) {
                                    if (schedule.status == "Pending" || schedule.status == "Accepted" || schedule.status == "Rejected" || schedule.status == "Delete" || schedule.status == "Request") {
                                        hasSpecificStatusBookings = true
                                        populateList(schedule)
                                    }
                                }

                                if (hasSpecificStatusBookings) {
                                    // If there are bookings with the specified statuses, show the appropriate views
                                    showToast("Bookings fetched successfully")
                                    loadingCARD.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                    tv_myAppointments.visibility = View.VISIBLE
                                } else {
                                    // If there are no bookings with the specified statuses, show the appropriate views
                                    showToast("No Appointments")
                                    tv_myAppointments.visibility = View.GONE
                                    loadingCARD.visibility = View.GONE
                                    noAppointsCARD.visibility = View.VISIBLE
                                    adapter.notifyDataSetChanged() // Refresh the RecyclerView
                                }
                            }
                        } else {
                            showToast("Failed to fetch data")
                            noAppointsCARD.visibility = View.VISIBLE
                            loadingCARD.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                        showToast("Failed to fetch data: ${t.message}")
                        noAppointsCARD.visibility = View.VISIBLE
                        loadingCARD.visibility = View.GONE
                    }

                    private fun showToast(message: String) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }


    private fun populateList(schedule: Schedule) {
        if (schedule.status == "Pending" || schedule.status == "Accepted" || schedule.status == "Rejected" || schedule.status == "Delete" || schedule.status == "Request") {
            // Fetch doctor details
            apiService.getDoctorDetails(schedule.doctorId ?: "").enqueue(object :
                Callback<DoctorUsers> {
                override fun onResponse(call: Call<DoctorUsers>, response: Response<DoctorUsers>) {
                    val doctor = response.body()
                    doctor?.let {

                        mListBookings.add(
                            PatientBookingsData(
                                "Status: ${schedule.status}",
                                "${it.Fname} ${it.Lname}",
                                it.specialty, // Use doctor's specialty
                                "MD since ${it.md}", // Use doctor's MD year
                                it.email,
                                it.number,
                                "₱${it.consultPrice}",
                                if (it.f2f == true) "Face-to-Face Consultation" else "Online Consultation",
                                // Include "Online Consultation" string only when online is true
                                "Date: ${schedule.date}",
                                "Time: ${schedule.time}",
                                "# ${it.hn}, ${it.barangay}. ${it.municipality}, ${it.province}",
                                "DoctorId: ${schedule.doctorId}",
                                "",
                                "",
                                it.imageData,
                                "",
                                schedule._id
                            )
                        )
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<DoctorUsers>, t: Throwable) {
                    // Handle failure
                }
            })
        }
    }





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