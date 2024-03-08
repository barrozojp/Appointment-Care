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

class HistoryDoctorAdapter(

    var mListHistory: List<HistoryDoctorData>,
    private val listener: HistoryDoctorFragment,
) : RecyclerView.Adapter<HistoryDoctorAdapter.doctorHistoryViewHolder>() {



    inner class doctorHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val status: TextView = itemView.findViewById(R.id.tv_Status)
        val profilePic: ImageView = itemView.findViewById(R.id.personProfilePic)
        val personName: TextView = itemView.findViewById(R.id.personName)
        val email: TextView = itemView.findViewById(R.id.personEmail)
        val personNumber: TextView = itemView.findViewById(R.id.personNumber)
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

        fun bind(historyDoctorData: HistoryDoctorData) {
            Glide.with(itemView)
                .load(historyDoctorData.imageData) // Use the imageData URL directly
                .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image
                .into(profilePic)

            status.text = historyDoctorData.status
            personName.text = historyDoctorData.fullName
            email.text = historyDoctorData.email
            personNumber.text = historyDoctorData.number
            tv_observation.text = historyDoctorData.observation
            tv_prescription.text = historyDoctorData.prescription

            setSymptomsVisibility(historyDoctorData.symptoms)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDoctorAdapter.doctorHistoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.doctor_history_items, parent, false)
        return doctorHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryDoctorAdapter.doctorHistoryViewHolder, position: Int) {
        holder.bind(mListHistory[position])
    }

    override fun getItemCount(): Int {
        return mListHistory.size
    }
}