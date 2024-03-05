package com.codeofduty.appointcare.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.codeofduty.appointcare.R


class AcceptedAdapter(var mListAccepted: List<AcceptedBookingsData>) :
    RecyclerView.Adapter<AcceptedAdapter.AcceptedViewHolder>() {


    inner class AcceptedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusBookings: TextView = itemView.findViewById(R.id.tv_Status)
        val profilePic: ImageView = itemView.findViewById(R.id.profilePic)
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val number: TextView = itemView.findViewById(R.id.phonenum)
        val email: TextView = itemView.findViewById(R.id.email)
        val online: TextView = itemView.findViewById(R.id.tv_consultationType)
        val time: TextView = itemView.findViewById(R.id.tv_time)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val bookingsID: TextView = itemView.findViewById(R.id.tv_bookID)
        val patientID: TextView = itemView.findViewById(R.id.tv_patientID)
        val consultBTN: Button = itemView.findViewById(R.id.consultBTN)

        init {
            consultBTN.setOnClickListener {
                // Get the patient ID from the current item
                val patientId = mListAccepted[adapterPosition].patientId

                // Start ConsultationFragment and pass patient details as arguments
                val fragment = patientId?.let { it1 ->
                    patientName?.let { it2 ->
                        email?.let { it3 ->
                            number?.let { it4 ->
                                ConsultationFragment.newInstance(
                                    it1,
                                    it2.text.toString(),
                                    it3.text.toString(),
                                    it4.text.toString()
                                )
                            }
                        }
                    }
                }
                val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                if (fragment != null) {
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }


        fun bind(acceptedBookingsData: AcceptedBookingsData) {

            statusBookings.text = acceptedBookingsData.status
            patientName.text = acceptedBookingsData.fullName
            number.text = acceptedBookingsData.number
            email.text = acceptedBookingsData.email
            online.text = acceptedBookingsData.online
            time.text = acceptedBookingsData.time
            date.text = acceptedBookingsData.date
            bookingsID.text = acceptedBookingsData.bookingsID
            patientID.text = acceptedBookingsData.patientId // Set patient ID here

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptedAdapter.AcceptedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.accepted_appoints_items, parent, false)
        return AcceptedViewHolder(view)
    }
    override fun onBindViewHolder(holder: AcceptedAdapter.AcceptedViewHolder, position: Int) {
        holder.bind(mListAccepted[position])
    }

    override fun getItemCount(): Int {
        return mListAccepted.size
    }
}