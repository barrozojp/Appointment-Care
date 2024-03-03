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
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.ApiService
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.databinding.FragmentMyConsultationBinding
import com.codeofduty.appointcare.models.MyBookings
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyConsultationFragment : Fragment() {

    private lateinit var apiService: ApiService
    private lateinit var noConsultationCARD: CardView
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
    private lateinit var tv_observation: TextView
    private lateinit var tv_prescription: TextView

    //SYMPTOMS

    private lateinit var tv_Cough: TextView
    private lateinit var tv_PainInBone: TextView
    private lateinit var tv_tiredness: TextView
    private lateinit var tv_UnexplainedWeightLoss: TextView
    private lateinit var tv_Paleness: TextView
    private lateinit var tv_UnexplainedFever: TextView
    private lateinit var tv_Bruising: TextView
    private lateinit var tv_FrequentInfection: TextView
    private lateinit var tv_UnexplainedRash: TextView
    private lateinit var tv_ShortnessOfBreath: TextView
    private lateinit var tv_DrenchingNightSweats: TextView
    private lateinit var tv_LumpsOfSwelling: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_consultation, container, false)

        // Initialize ApiService
        apiService = RetrofitClient.createService(requireContext())

        // Find the card view
        noConsultationCARD = view.findViewById(R.id.noConsultationCARD)
        yourBookingCard = view.findViewById(R.id.YourConsultationCARD)
        loadingCARD = view.findViewById(R.id.loadingCARD)

        // Find the time and date TextViews
        tv_time = view.findViewById(R.id.tv_time)
        tv_date = view.findViewById(R.id.tv_date)

        tv_Status = view.findViewById(R.id.tv_Status)


        // Find the F2F and Online TextViews
        tv_F2F = view.findViewById(R.id.tv_F2F)
        tv_Online = view.findViewById(R.id.tv_Online)

        // Fetch bookings for the logged-in user
        fetchConsultationForCurrentUser()

        // Find the TextViews for user info
        tv_nameUser = view.findViewById(R.id.tv_nameUser)
        tv_emailUser = view.findViewById(R.id.tv_emailUser)
        tv_phoneUser = view.findViewById(R.id.tv_phoneUser)

        // Fetch user info and set TextViews
        setUserInfo()

        tv_observation = view.findViewById(R.id.tv_observation)
        tv_prescription = view.findViewById(R.id.tv_prescription)


        //CONSULTATION TextViews

        tv_nameDoctor = view.findViewById(R.id.tv_nameDoctor)
        tv_specialtyDoctor = view.findViewById(R.id.tv_specialtyDoctor)
        tv_mdYearDoctor = view.findViewById(R.id.tv_mdYearDoctor)
        tv_emailDoctor = view.findViewById(R.id.tv_emailDoctor)
        tv_numberDoctor = view.findViewById(R.id.tv_numberDoctor)
        tv_priceDoctor = view.findViewById(R.id.tv_priceDoctor)


        // Find the TextViews for symptoms
        tv_Cough = view.findViewById(R.id.tv_Cough)
        tv_PainInBone = view.findViewById(R.id.tv_PainInBone)
        tv_tiredness = view.findViewById(R.id.tv_tiredness)
        tv_UnexplainedWeightLoss = view.findViewById(R.id.tv_UnexplainedWeightLoss)
        tv_Paleness = view.findViewById(R.id.tv_Paleness)
        tv_UnexplainedFever = view.findViewById(R.id.tv_UnexplainedFever)
        tv_Bruising = view.findViewById(R.id.tv_Bruising)
        tv_FrequentInfection = view.findViewById(R.id.tv_FrequentInfection)
        tv_UnexplainedRash = view.findViewById(R.id.tv_UnexplainedRash)
        tv_ShortnessOfBreath = view.findViewById(R.id.tv_ShortnessOfBreath)
        tv_DrenchingNightSweats = view.findViewById(R.id.tv_DrenchingNightSweats)
        tv_LumpsOfSwelling = view.findViewById(R.id.tv_LumpsOfSwelling)

        return view
    }

    private fun fetchConsultationForCurrentUser() {
        val user = getUserData()
        user?.let { userData ->
            userData._id?.let { userId ->
                apiService.getBookings(userId).enqueue(object : Callback<MyBookings> {
                    override fun onResponse(call: Call<MyBookings>, response: Response<MyBookings>) {
                        if (response.isSuccessful) {
                            val myBookings = response.body()
                            if (myBookings != null && myBookings.schedules.isNotEmpty()) {
                                val booking = myBookings.schedules[0]
                                // Consultation found, make the card visible
                                yourBookingCard.visibility = View.VISIBLE
                                val doctorId = booking.doctorId
                                doctorId?.let { id ->
                                    // Fetch doctor details using the doctorId
                                    fetchDoctorDetails(id)
                                }

                                // Set visibility of F2F and Online TextViews based on response
                                if (booking.f2f == true) {
                                    tv_F2F.visibility = View.VISIBLE
                                } else {
                                    tv_F2F.visibility = View.GONE
                                }

                                if (booking.online == true) {
                                    tv_Online.visibility = View.VISIBLE
                                } else {
                                    tv_Online.visibility = View.GONE
                                }

                                // Set time and date
                                tv_time.text = booking.time
                                tv_date.text = booking.date

                                val statusLabelText = "Status: "
                                val status = booking.status
                                val fullStatusText = "$statusLabelText$status"
                                tv_Status.text = fullStatusText

                                // Check if observation, symptoms, or prescription are null
                                if (booking.observation == null || booking.symptoms == null || booking.prescription == null) {
                                    // If any of them is null, hide the booking card
                                    yourBookingCard.visibility = View.GONE
                                    noConsultationCARD.visibility = View.VISIBLE
                                    loadingCARD.visibility = View.GONE


                                    Toast.makeText(requireContext(), "No consultation found", Toast.LENGTH_SHORT).show()

                                } else {
                                    // Otherwise, set consultation details
                                    val observationLabelText = "Observation:  "
                                    val observation = booking.observation
                                    val observationText = "$observationLabelText$observation"
                                    tv_observation.text = observationText

                                    val prescriptionLabelText = "Prescription:  "
                                    val prescription = booking.prescription
                                    val prescriptionText = "$prescriptionLabelText$prescription"
                                    tv_prescription.text = prescriptionText

                                    // Set symptoms visibility
                                    setSymptomsVisibility(booking.symptoms)

                                    noConsultationCARD.visibility = View.GONE
                                    loadingCARD.visibility = View.GONE

                                }
                            } else {
                                // No bookings found, hide the card
                                yourBookingCard.visibility = View.GONE

                            }
                        } else {
                            // Handle unsuccessful response
                            Toast.makeText(requireContext(), "No consultation found", Toast.LENGTH_SHORT).show()
                            noConsultationCARD.visibility = View.VISIBLE
                            loadingCARD.visibility = View.GONE


                        }
                    }

                    override fun onFailure(call: Call<MyBookings>, t: Throwable) {
                        // Handle failure
                        Toast.makeText(requireContext(), "Failed to fetch bookings", Toast.LENGTH_SHORT).show()
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

    private fun setSymptomsVisibility(symptoms: List<String>?) {
        symptoms?.let {
            for (symptom in symptoms) {
                when (symptom) {
                    "cough" -> tv_Cough.visibility = View.VISIBLE
                    "painInBone" -> tv_PainInBone.visibility = View.VISIBLE
                    "tiredness" -> tv_tiredness.visibility = View.VISIBLE
                    "unexplainedWeightLoss" -> tv_UnexplainedWeightLoss.visibility = View.VISIBLE
                    "paleness" -> tv_Paleness.visibility = View.VISIBLE
                    "unexplainedFever" -> tv_UnexplainedFever.visibility = View.VISIBLE
                    "bruising" -> tv_Bruising.visibility = View.VISIBLE
                    "frequentInfection" -> tv_FrequentInfection.visibility = View.VISIBLE
                    "unexplainedRash" -> tv_UnexplainedRash.visibility = View.VISIBLE
                    "shortnessOfBreath" -> tv_ShortnessOfBreath.visibility = View.VISIBLE
                    "drenchingNightSweats" -> tv_DrenchingNightSweats.visibility = View.VISIBLE
                    "lumpsOfSwelling" -> tv_LumpsOfSwelling.visibility = View.VISIBLE
                }
            }
        }
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