package com.codeofduty.appointcare.activities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeofduty.appointcare.R

class PatientConsultationAdapter(var mListConsultation: List<PatientConsultationData>) :
    RecyclerView.Adapter<PatientConsultationAdapter.patientConsultationViewHolder>() {


    inner class patientConsultationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

        var tv_observation: TextView = itemView.findViewById(R.id.tv_observation)
        var tv_prescription: TextView = itemView.findViewById(R.id.tv_prescription)


        //LOGGED IN USER INFO
        val personProfilePic: ImageView = itemView.findViewById(R.id.personProfilePic)
        val personName: TextView = itemView.findViewById(R.id.personName)
        val personEmail: TextView = itemView.findViewById(R.id.personEmail)
        val personNumber: TextView = itemView.findViewById(R.id.personNumber)

        var tv_Cough: TextView = itemView.findViewById(R.id.tv_Cough)
        var tv_PainInBone: TextView = itemView.findViewById(R.id.tv_PainInBone)
        var tv_tiredness: TextView = itemView.findViewById(R.id.tv_tiredness)
        var tv_UnexplainedWeightLoss: TextView = itemView.findViewById(R.id.tv_UnexplainedWeightLoss)
        var tv_Paleness: TextView = itemView.findViewById(R.id.tv_Paleness)
        var tv_UnexplainedFever: TextView = itemView.findViewById(R.id.tv_UnexplainedFever)
        var tv_Bruising: TextView = itemView.findViewById(R.id.tv_Bruising)
        var tv_FrequentInfection: TextView = itemView.findViewById(R.id.tv_FrequentInfection)
        var tv_UnexplainedRash: TextView = itemView.findViewById(R.id.unexplainedRash)
        var tv_ShortnessOfBreath: TextView = itemView.findViewById(R.id.tv_ShortnessOfBreath)
        var tv_DrenchingNightSweats: TextView = itemView.findViewById(R.id.tv_DrenchingNightSweats)
        var tv_LumpsOfSwelling: TextView = itemView.findViewById(R.id.tv_LumpsOfSwelling)


        fun bind(patientConsultationData: PatientConsultationData) {
            Glide.with(itemView)
                .load(patientConsultationData.imageData) // Use the imageData URL directly
                .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image
                .into(profilePic)

            status.text = patientConsultationData.status
            DoctorName.text = patientConsultationData.fullName
            DoctorSpecialty.text = patientConsultationData.specialty
            mdYear.text = patientConsultationData.mdYear
            email.text = patientConsultationData.email
            DoctorNumber.text = patientConsultationData.number
            DoctorConsultPrice.text = patientConsultationData.consultPrice
            face2face.text = patientConsultationData.f2f.toString()
            date.text = patientConsultationData.date
            time.text = patientConsultationData.time
            address.text = patientConsultationData.address


            tv_observation.text = patientConsultationData.observation
            tv_prescription.text = patientConsultationData.prescription

            tv_Cough.text = patientConsultationData.symptoms.toString()
            tv_PainInBone.text = patientConsultationData.symptoms.toString()
            tv_tiredness.text = patientConsultationData.symptoms.toString()
            tv_UnexplainedWeightLoss.text = patientConsultationData.symptoms.toString()
            tv_Paleness.text = patientConsultationData.symptoms.toString()
            tv_UnexplainedFever.text = patientConsultationData.symptoms.toString()
            tv_Bruising.text = patientConsultationData.symptoms.toString()
            tv_FrequentInfection.text = patientConsultationData.symptoms.toString()
            tv_UnexplainedRash.text = patientConsultationData.symptoms.toString()
            tv_ShortnessOfBreath.text = patientConsultationData.symptoms.toString()
            tv_DrenchingNightSweats.text = patientConsultationData.symptoms.toString()
            tv_LumpsOfSwelling.text = patientConsultationData.symptoms.toString()





            // Set logged-in user information
            val sharedPreferences =
                itemView.context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val profilePicUrl = sharedPreferences.getString("imageData", "")
            val name = sharedPreferences.getString(
                "fname",
                ""
            ) + " " + sharedPreferences.getString("lname", "")
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientConsultationAdapter.patientConsultationViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.patient_bookings_items, parent, false)
        return patientConsultationViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientConsultationAdapter.patientConsultationViewHolder, position: Int) {
        holder.bind(mListConsultation[position])
    }

    override fun getItemCount(): Int {
        return mListConsultation.size
    }
}