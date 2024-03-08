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


class HistoryPatientAdapter(
    var mListHistory: List<HistoryPatientData>,
    private val listener: HistoryPatientFragment,
) : RecyclerView.Adapter<HistoryPatientAdapter.patientHistoryViewHolder>() {



    inner class patientHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        val tv_observation: TextView = itemView.findViewById(R.id.tv_observation)
        val tv_prescription: TextView = itemView.findViewById(R.id.tv_prescription)


        //SYMPTOMS
        var tv_Cough: TextView = itemView.findViewById(R.id.tv_Cough)
        var tv_PainInBone: TextView = itemView.findViewById(R.id.tv_PainInBone)
        var tv_tiredness: TextView = itemView.findViewById(R.id.tv_tiredness)
        var tv_UnexplainedWeightLoss: TextView = itemView.findViewById(R.id.tv_UnexplainedWeightLoss)
        var tv_Paleness: TextView = itemView.findViewById(R.id.tv_Paleness)
        var tv_UnexplainedFever: TextView = itemView.findViewById(R.id.tv_UnexplainedFever)
        var tv_Bruising: TextView = itemView.findViewById(R.id.tv_Bruising)
        var tv_FrequentInfection: TextView = itemView.findViewById(R.id.tv_FrequentInfection)
        var tv_UnexplainedRash: TextView = itemView.findViewById(R.id.tv_UnexplainedRash)
        var tv_ShortnessOfBreath: TextView = itemView.findViewById(R.id.tv_ShortnessOfBreath)
        var tv_DrenchingNightSweats: TextView = itemView.findViewById(R.id.tv_DrenchingNightSweats)
        var tv_LumpsOfSwelling: TextView = itemView.findViewById(R.id.tv_LumpsOfSwelling)

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



        //LOGGED IN USER INFO
        val personProfilePic: ImageView = itemView.findViewById(R.id.personProfilePic)
        val personName: TextView = itemView.findViewById(R.id.personName)
        val personEmail: TextView = itemView.findViewById(R.id.personEmail)
        val personNumber: TextView = itemView.findViewById(R.id.personNumber)


        fun bind(historyPatientData: HistoryPatientData) {
            Glide.with(itemView)
                .load(historyPatientData.imageData) // Use the imageData URL directly
                .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image
                .into(profilePic)

            status.text = historyPatientData.status
            DoctorName.text = historyPatientData.fullName
            DoctorSpecialty.text = historyPatientData.specialty
            mdYear.text = historyPatientData.mdYear
            email.text = historyPatientData.email
            DoctorNumber.text = historyPatientData.number
            DoctorConsultPrice.text = historyPatientData.consultPrice
            face2face.text = historyPatientData.f2f.toString()
            date.text = historyPatientData.date
            time.text = historyPatientData.time
            address.text = historyPatientData.address
            tv_observation.text = historyPatientData.observation
            tv_prescription.text = historyPatientData.prescription

            setSymptomsVisibility(historyPatientData.symptoms)


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryPatientAdapter.patientHistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.patient_history_items, parent, false)
        return patientHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryPatientAdapter.patientHistoryViewHolder, position: Int) {
        holder.bind(mListHistory[position])
    }

    override fun getItemCount(): Int {
        return mListHistory.size
    }
}