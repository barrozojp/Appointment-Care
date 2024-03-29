package com.codeofduty.appointcare.activities

import DoctorUsers
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.ApiService
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.models.MyBookings
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyBookingsFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var noBookingsCard: CardView
    private lateinit var yourBookingCard: CardView
    private lateinit var loadingCARD: CardView
    private lateinit var tv_time: TextView
    private lateinit var tv_date: TextView
    private lateinit var tv_F2F: TextView
    private lateinit var tv_Online: TextView
    private lateinit var tv_nameUser: TextView
    private lateinit var tv_emailUser: TextView
    private lateinit var tv_phoneUser: TextView
    private lateinit var tv_nameDoctor: TextView
    private lateinit var tv_specialtyDoctor: TextView
    private lateinit var tv_mdYearDoctor: TextView
    private lateinit var tv_emailDoctor: TextView
    private lateinit var tv_numberDoctor: TextView
    private lateinit var tv_priceDoctor: TextView
    private lateinit var tv_Status: TextView
    private lateinit var DoctorProfilePic: ImageView





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_bookings, container, false)

        // Initialize ApiService
        apiService = RetrofitClient.createService(requireContext())

        // Find the card view
        noBookingsCard = view.findViewById(R.id.noBookingsCARD)
        yourBookingCard = view.findViewById(R.id.YourBookingsCARD)
        loadingCARD = view.findViewById(R.id.loadingCARD)


        // Find the time and date TextViews
        tv_time = view.findViewById(R.id.tv_time)
        tv_date = view.findViewById(R.id.tv_date)

        tv_Status = view.findViewById(R.id.tv_Status)


        // Find the F2F and Online TextViews
        tv_F2F = view.findViewById(R.id.tv_F2F)
        tv_Online = view.findViewById(R.id.tv_Online)

        // Fetch bookings for the logged-in user
        fetchBookingsForCurrentUser()

        // Find the TextViews for user info
        tv_nameUser = view.findViewById(R.id.tv_nameUser)
        tv_emailUser = view.findViewById(R.id.tv_emailUser)
        tv_phoneUser = view.findViewById(R.id.tv_phoneUser)

        // Fetch user info and set TextViews
        setUserInfo()

        tv_nameDoctor = view.findViewById(R.id.tv_nameDoctor)
        tv_specialtyDoctor = view.findViewById(R.id.tv_specialtyDoctor)
        tv_mdYearDoctor = view.findViewById(R.id.tv_mdYearDoctor)
        tv_emailDoctor = view.findViewById(R.id.tv_emailDoctor)
        tv_numberDoctor = view.findViewById(R.id.tv_numberDoctor)
        tv_priceDoctor = view.findViewById(R.id.tv_priceDoctor)

        DoctorProfilePic = view.findViewById(R.id.DoctorProfilePic)



        return view
    }

    private fun fetchBookingsForCurrentUser() {
        val user = getUserData()
        user?.let { userData ->
            userData._id?.let { userId ->
                apiService.getBookings(userId).enqueue(object : Callback<MyBookings> {
                    override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                        if (response.isSuccessful) {
                            val myBookings = response.body()
                            if (myBookings != null && myBookings.schedules.isNotEmpty()) {
                                // Bookings found, make the card gone
                                noBookingsCard.visibility = View.GONE
                                yourBookingCard.visibility = View.VISIBLE


                                val doctorId = myBookings.schedules[0].doctorId
                                doctorId?.let { id ->
                                    // Fetch doctor details using the doctorId
                                    fetchDoctorDetails(id)
                                }

                                // Set visibility of F2F and Online TextViews based on response
                                if (myBookings.schedules[0].f2f == true) {
                                    tv_F2F.visibility = View.VISIBLE
                                } else {
                                    tv_F2F.visibility = View.GONE
                                }

                                if (myBookings.schedules[0].online == true) {
                                    tv_Online.visibility = View.VISIBLE
                                } else {
                                    tv_Online.visibility = View.GONE
                                }

                                // Set time and date
                                tv_time.text = myBookings.schedules[0].time
                                tv_date.text = myBookings.schedules[0].date

                                val statusLabelText = "Status: "
                                val status = myBookings.schedules[0].status
                                val fullStatusText = "$statusLabelText$status"
                                tv_Status.text = fullStatusText

                            } else {
                                // No bookings found, dont hide the card
                                noBookingsCard.visibility = View.VISIBLE
                                loadingCARD.visibility = View.GONE
                                yourBookingCard.visibility = View.GONE
                            }
                        } else {
                            // Handle unsuccessful response
                            Toast.makeText(requireContext(), "No bookings found", Toast.LENGTH_SHORT).show()
                            noBookingsCard.visibility = View.VISIBLE

                        }
                    }

                    override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                        // Handle failure
                        Toast.makeText(requireContext(), "No bookings found", Toast.LENGTH_SHORT).show()
                        noBookingsCard.visibility = View.VISIBLE
                        loadingCARD.visibility = View.GONE


                    }
                })
            }
        }
    }
    private fun fetchDoctorDetails(doctorId: String) {
        apiService.getDoctorDetails(doctorId).enqueue(object : Callback<DoctorUsers> {
            override fun onResponse(call: Call<DoctorUsers>, response: Response<DoctorUsers>) {
                if (response.isSuccessful) {
                    val doctorDetails = response.body()
                    doctorDetails?.let {
                        // Populate doctor details in the layout
                        tv_nameDoctor.text = "${it.Fname} ${it.Lname}"
                        tv_specialtyDoctor.text = it.specialty
                        tv_mdYearDoctor.text = "MD since ${it.md}"
                        tv_emailDoctor.text = it.email
                        tv_numberDoctor.text = it.number
                        tv_priceDoctor.text = it.consultPrice

                        // Load and set the doctor's profile image
                        val imageData = it.imageData
                        if (!imageData.isNullOrEmpty()) {
                            Glide.with(requireContext())
                                .load(imageData)
                                .placeholder(R.drawable.doctor_profile)
                                .into(DoctorProfilePic)
                        } else {
                            // Handle null or empty imageData
                            Toast.makeText(requireContext(), "No image data found", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(requireContext(), "Failed to fetch doctor details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DoctorUsers>, t: Throwable) {
                // Handle failure
                Toast.makeText(requireContext(), "Failed to fetch doctor details", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun setUserInfo() {
        // Retrieve user info from SharedPreferences or wherever you store them
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val Fname = sharedPreferences.getString("fname", "")
        val Lname = sharedPreferences.getString("lname", "")
        val email = sharedPreferences.getString("email", "")
        val number = sharedPreferences.getString("number", "")

        // Combine first name and last name to get full name
        val fullName = "$Fname $Lname"

        // Set the TextViews with the retrieved values
        tv_nameUser.text = fullName
        tv_emailUser.text = email
        tv_phoneUser.text = number
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