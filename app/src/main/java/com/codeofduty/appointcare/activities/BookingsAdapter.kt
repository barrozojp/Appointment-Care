package com.codeofduty.appointcare.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codeofduty.appointcare.R

class BookingsAdapter(var mList: List<DoctorBookingsData>) :
    RecyclerView.Adapter<BookingsAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val statusBookings: TextView = itemView.findViewById(R.id.tv_Status)
        val profilePic: ImageView = itemView.findViewById(R.id.profilePic)
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val number: TextView = itemView.findViewById(R.id.phonenum)
        val email: TextView = itemView.findViewById(R.id.email)
        val online: TextView = itemView.findViewById(R.id.tv_online)
        val f2f: TextView = itemView.findViewById(R.id.tv_f2f)
        val time: TextView = itemView.findViewById(R.id.tv_time)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val bookingsID: TextView = itemView.findViewById(R.id.tv_bookID)
        val AcceptBTN: Button = itemView.findViewById(R.id.AcceptBTN)
        val RejectBTN: Button = itemView.findViewById(R.id.RejectBTN)


        fun bind(doctorBookingsData: DoctorBookingsData) {

            statusBookings.text = doctorBookingsData.status
            patientName.text = doctorBookingsData.fullName
            number.text = doctorBookingsData.number
            email.text = doctorBookingsData.email
            online.text = doctorBookingsData.online
            f2f.text = doctorBookingsData.f2f
            time.text = doctorBookingsData.time
            date.text = doctorBookingsData.date
            bookingsID.text = doctorBookingsData.bookingsID
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingsAdapter.LanguageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.pending_appoints_items, parent, false)
        return LanguageViewHolder(view)
    }
    override fun onBindViewHolder(holder: BookingsAdapter.LanguageViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}