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


class DoctorsAcceptedPatientFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AcceptedAdapter
    private var mListAccepted = ArrayList<AcceptedBookingsData>()
    private lateinit var apiService: ApiService
    private lateinit var loadingCARD: CardView
    private lateinit var noAcceptedAppointsCARD: CardView
    private lateinit var tv_AcceptedAppointments: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doctors_accepted_patient, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewAcceptedAppoints)
        loadingCARD = view.findViewById(R.id.loadingCARD)
        tv_AcceptedAppointments = view.findViewById(R.id.tv_acceptedAppointments)
        noAcceptedAppointsCARD = view.findViewById(R.id.noAcceptedAppointsCARD)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AcceptedAdapter(mListAccepted)
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
                apiService.getAcceptedBookings(_id).enqueue(object : Callback<MyBookings> {
                    override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                        if (response.isSuccessful) {
                            val myBookings = response.body()

                            myBookings?.let { bookings ->
                                val acceptedSchedules = bookings.schedules.filter { it.status == "Accepted" }
                                if (acceptedSchedules.isNotEmpty()) {
                                    populateList(acceptedSchedules)
                                    showToast("Accepted Bookings fetched successfully")
                                    loadingCARD.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                    tv_AcceptedAppointments.visibility = View.VISIBLE
                                } else {
                                    showToast("No Accepted Appointments")
                                    noAcceptedAppointsCARD.visibility = View.VISIBLE
                                    loadingCARD.visibility = View.GONE
                                    adapter.notifyDataSetChanged() // Refresh the RecyclerView

                                }
                            }
                        } else {
                            showToast("Failed to fetch data")
                            noAcceptedAppointsCARD.visibility = View.VISIBLE
                            loadingCARD.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                        showToast("Failed to fetch data: ${t.message}")
                        noAcceptedAppointsCARD.visibility = View.VISIBLE
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

            mListAccepted.add(
                AcceptedBookingsData(
                    "Status: ${schedule.status}",
                    "${schedule.fullName}",
                    "${schedule.number}",
                    "Email: ${schedule.email}",
                    consultationType,
                    "${schedule.date}",
                    "Time: ${schedule.time}",
                    "BookingID: ${schedule._id}",
                    "${schedule.patientId}",
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