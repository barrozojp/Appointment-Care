package com.codeofduty.appointcare.activities

import android.app.AlertDialog
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
import com.codeofduty.appointcare.models.UpdateBookingStatusRequest
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class DoctorRejectedPatientsFragment : Fragment(),BookingStatusListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RejectedAdapter
    private var mListRejected = ArrayList<RejectedBookingsData>()
    private lateinit var apiService: ApiService
    private lateinit var loadingCARD: CardView
    private lateinit var noRejectedAppointsCARD: CardView
    private lateinit var tv_rejectedAppointments: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doctor_rejected_patients, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewRejectedAppoints)
        loadingCARD = view.findViewById(R.id.loadingCARD)
        tv_rejectedAppointments = view.findViewById(R.id.tv_rejectedAppointments)
        noRejectedAppointsCARD = view.findViewById(R.id.noRejectedAppointsCARD)


        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = RejectedAdapter(mListRejected, this)
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
                apiService.getRejectedBookings(_id).enqueue(object : Callback<MyBookings> {
                    override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                        if (response.isSuccessful) {
                            val myBookings = response.body()

                            myBookings?.let { bookings ->
                                val rejectedSchedules = bookings.schedules.filter { it.status == "Rejected" }
                                if (rejectedSchedules.isNotEmpty()) {
                                    populateList(rejectedSchedules)
                                    showToast("Rejected Bookings fetched successfully")
                                    loadingCARD.visibility = View.GONE
                                    recyclerView.visibility = View.VISIBLE
                                    tv_rejectedAppointments.visibility = View.VISIBLE
                                } else {
                                    showToast("No Rejected Appointments")
                                    noRejectedAppointsCARD.visibility = View.VISIBLE
                                    loadingCARD.visibility = View.GONE
                                    adapter.notifyDataSetChanged() // Refresh the RecyclerView

                                }
                            }
                        } else {
                            showToast("Failed to fetch data")
                            noRejectedAppointsCARD.visibility = View.VISIBLE
                            loadingCARD.visibility = View.GONE
                        }
                    }

                    override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                        showToast("Failed to fetch data: ${t.message}")
                        noRejectedAppointsCARD.visibility = View.VISIBLE
                        loadingCARD.visibility = View.GONE
                    }

                    private fun showToast(message: String) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onUpdateBookingStatus(patientId: String, status: String, loadingDialog: AlertDialog) {
        // Create an instance of UpdateBookingStatusRequest with the provided patientId and status
        val requestBody = UpdateBookingStatusRequest(patientId, status)

        // Get the user data
        val userData = getUserData()
        userData?.let { user ->
            val userId = user._id
            userId?.let {
                // Call the API to update the booking status
                apiService.updateBookingStatus(it, requestBody).enqueue(object : Callback<MyBookings> {
                    override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                        if (response.isSuccessful) {
                            showToast("Booking status updated successfully")
                            navigateToHomeFragment()
                            loadingDialog.dismiss()
                        } else {
                            // Update failed, show an error message
                            showToast("Failed to update booking status")
                        }
                    }

                    override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                        // Failure in API call, show an error message
                        showToast("Failed to update booking status: ${t.message}")
                    }

                    private fun showToast(message: String) {
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun navigateToHomeFragment() {
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, HomeFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun populateList(schedules: List<Schedule>) {
        for (schedule in schedules) {

            val consultationType = if (schedule.online == true) {
                "Online Consultation"
            } else {
                "Face-to-Face Consultation"
            }

            mListRejected.add(
                RejectedBookingsData(
                    "Status: ${schedule.status}",
                    schedule.imageData,
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