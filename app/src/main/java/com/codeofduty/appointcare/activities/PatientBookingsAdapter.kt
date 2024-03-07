package com.codeofduty.appointcare.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeofduty.appointcare.R

class PatientBookingsAdapter(var mListBookings: List<PatientBookingsData>) :
    RecyclerView.Adapter<PatientBookingsAdapter.patientBookingsViewHolder>(){


    inner class patientBookingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val status: TextView = itemView.findViewById(R.id.tv_Status)
        val profilePic: ImageView = itemView.findViewById(R.id.profilePic)
        val DoctorName: TextView = itemView.findViewById(R.id.DoctorName)
        val DoctorSpecialty: TextView = itemView.findViewById(R.id.DoctorSpecialty)
        val mdYear: TextView = itemView.findViewById(R.id.DoctorMDYear)
        val email: TextView = itemView.findViewById(R.id.DoctorEMAIL)
        val DoctorNumber: TextView = itemView.findViewById(R.id.DoctorNumber)
        val DoctorConsultPrice: TextView = itemView.findViewById(R.id.DoctorConsultPrice)
        val face2face: TextView = itemView.findViewById(R.id.face2face)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val time: TextView = itemView.findViewById(R.id.tv_time)
        val address: TextView = itemView.findViewById(R.id.tv_location)
        val bookingId: TextView = itemView.findViewById(R.id.tv_bookID)


        //LOGGED IN USER INFO
        val personProfilePic: ImageView = itemView.findViewById(R.id.personProfilePic)
        val personName: TextView = itemView.findViewById(R.id.personName)
        val personEmail: TextView = itemView.findViewById(R.id.personEmail)
        val personNumber: TextView = itemView.findViewById(R.id.personNumber)


        fun bind(patientBookingsData: PatientBookingsData) {
            Glide.with(itemView)
                .load(patientBookingsData.imageData) // Use the imageData URL directly
                .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image
                .into(profilePic)

            status.text = patientBookingsData.status
            DoctorName.text = patientBookingsData.fullName
            DoctorSpecialty.text = patientBookingsData.specialty
            mdYear.text = patientBookingsData.mdYear
            email.text = patientBookingsData.email
            DoctorNumber.text = patientBookingsData.number
            DoctorConsultPrice.text = patientBookingsData.consultPrice
            face2face.text = patientBookingsData.f2f.toString()
            date.text = patientBookingsData.date
            time.text = patientBookingsData.time
            address.text = patientBookingsData.address


            // Set logged-in user information
            val sharedPreferences = itemView.context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val profilePicUrl = sharedPreferences.getString("imageData", "")
            val name = sharedPreferences.getString("fname", "") + " " + sharedPreferences.getString("lname", "")
            val email = sharedPreferences.getString("email", "")
            val number = sharedPreferences.getString("number", "")

            Glide.with(itemView)
                .load(profilePicUrl) // Load user profile pic
                .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image
                .into(personProfilePic)

            personName.text = name
            personEmail.text = email
            personNumber.text = number


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientBookingsAdapter.patientBookingsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.patient_bookings_items, parent, false)
        return patientBookingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientBookingsAdapter.patientBookingsViewHolder, position: Int) {
        holder.bind(mListBookings[position])
    }

    override fun getItemCount(): Int {
        return mListBookings.size
    }
}