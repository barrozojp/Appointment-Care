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


class HistoryDoctorFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryDoctorAdapter
    private var mListHistory = ArrayList<HistoryDoctorData>()
    private lateinit var apiService: ApiService
    private lateinit var loadingCARD: CardView
    private lateinit var noAppointsCARD: CardView
    private lateinit var tv_myAppointments: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history_doctor, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewMyHistory)
        loadingCARD = view.findViewById(R.id.loadingCARD)
        tv_myAppointments = view.findViewById(R.id.tv_myHistory)
        noAppointsCARD = view.findViewById(R.id.noHistoryCARD)

        // Initialize the ApiService
        apiService = RetrofitClient.getService()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = HistoryDoctorAdapter(mListHistory, this)
        recyclerView.adapter = adapter

        fetchData()

        return view
    }

    private fun fetchData() {
        val user = getUserData()
        user?.let { userData ->
            val userId = userData._id
            userId?.let { _id ->
                apiService.getBookingsHistoryPatient(_id).enqueue(object : Callback<MyBookings> {
                    override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                        if (response.isSuccessful) {
                            val myBookings = response.body()

                            myBookings?.let { bookings ->
                                var hasDoneBookings = false // Flag to track if any bookings have status "Done"
                                for (schedule in bookings.schedules) {
                                    if (schedule.status == "Done") {
                                        hasDoneBookings = true
                                        populateList(schedule)
                                    }
                                }

                                if (hasDoneBookings) {
                                    // If there are bookings with status "Done", show the appropriate views
                                    showToast("Bookings fetched successfully")
                                    loadingCARD.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                    tv_myAppointments.visibility = View.VISIBLE
                                } else {
                                    // If there are no bookings with status "Done", show the appropriate views
                                    showToast("No Appointments")
                                    tv_myAppointments.visibility = View.GONE
                                    loadingCARD.visibility = View.GONE
                                    noAppointsCARD.visibility = View.VISIBLE
                                    adapter.notifyDataSetChanged() // Refresh the RecyclerView
                                }
                            }
                        } else {
                            showToast("No Consulattion Histry")
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
        if (schedule.status == "Done") {
            // Fetch doctor details
            apiService.getDoctorDetails(schedule.patientId ?: "").enqueue(object :
                Callback<DoctorUsers> {
                override fun onResponse(call: Call<DoctorUsers>, response: Response<DoctorUsers>) {
                    val doctor = response.body()
                    doctor?.let {
                        mListHistory.add(
                            HistoryDoctorData(
                                "Status: ${schedule.status}",
                                "${it.Fname} ${it.Lname}",
                                it.email,
                                it.number,
                                "DoctorId: ${schedule.doctorId}",
                                "",
                                "",
                                "${schedule.imageData}",
                                "",
                                schedule._id, //THIS IS THE BOOKING ID
                                schedule.symptoms,
                                "Observation: ${schedule.observation}",
                                "Prescription: ${schedule.prescription}"
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
