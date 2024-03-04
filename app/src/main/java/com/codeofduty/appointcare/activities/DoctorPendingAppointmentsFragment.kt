package com.codeofduty.appointcare.activities

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

class DoctorPendingAppointmentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookingsAdapter
    private var mList = ArrayList<DoctorBookingsData>()
    private lateinit var apiService: ApiService
    private lateinit var loadingCARD: CardView
    private lateinit var noPendingAppointsCARD: CardView
    private lateinit var tv_pendingAppointments: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doctor_pending_appointments, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewPendingAppoints)
        loadingCARD = view.findViewById(R.id.loadingCARD)
        tv_pendingAppointments = view.findViewById(R.id.tv_pendingAppointments)
        noPendingAppointsCARD = view.findViewById(R.id.noPendingAppointsCARD)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BookingsAdapter(mList)
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
                apiService.getPendingBookings(_id).enqueue(object : Callback<MyBookings> {
                    override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                        if (response.isSuccessful) {
                            val myBookings = response.body()
                            myBookings?.let { bookings ->
                                val pendingSchedules = bookings.schedules.filter { it.status == "Pending" }
                                if (pendingSchedules.isEmpty()) {
                                    // No pending appointments, show message
                                    showToast("No Pending Appointments")
                                    noPendingAppointsCARD.visibility = View.VISIBLE
                                    loadingCARD.visibility = View.GONE

                                } else {
                                    // Pending appointments fetched, populate list
                                    populateList(pendingSchedules)
                                    showToast("Bookings fetched successfully")
                                    loadingCARD.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                    tv_pendingAppointments.visibility = View.VISIBLE
                                }
                            }
                        } else {
                            // Failed to fetch data, show message
                            showToast("Failed to fetch data")
                            noPendingAppointsCARD.visibility = View.VISIBLE
                            loadingCARD.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                        // Failed to fetch data, show message
                        showToast("Failed to fetch data: ${t.message}")
                        noPendingAppointsCARD.visibility = View.VISIBLE
                        loadingCARD.visibility = View.GONE
                    }

                    private fun showToast(message: String) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }





    private fun populateList(schedules: List<Schedule>) {
        for (schedule in schedules) {

            val consultationType = if (schedule.online == true) {
                "Online Consultation"
            } else {
                "Face-to-Face Consultation"
            }

            mList.add(
                DoctorBookingsData(
                    "Status: ${schedule.status}",
                    "${schedule.fullName}",
                    "${schedule.number}",
                    "Email: ${schedule.email}",
                    consultationType,
                    "${schedule.date}",
                    "Time: ${schedule.time}",
                    "BookingID: ${schedule._id}",
                    "",
                    "",
                )
            )
        }
        adapter.notifyDataSetChanged()
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