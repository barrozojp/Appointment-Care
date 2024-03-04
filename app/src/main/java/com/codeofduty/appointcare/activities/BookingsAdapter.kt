package com.codeofduty.appointcare.activities

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codeofduty.appointcare.R

class BookingsAdapter(var mList: List<DoctorBookingsData>, private val listener: DoctorPendingAppointmentsFragment) :
    RecyclerView.Adapter<BookingsAdapter.LanguageViewHolder>() {

    private fun showLoadingDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
        return dialog
    }
    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        val AcceptBTN: Button = itemView.findViewById(R.id.AcceptBTN)
        val RejectBTN: Button = itemView.findViewById(R.id.RejectBTN)


        fun bind(doctorBookingsData: DoctorBookingsData) {

            statusBookings.text = doctorBookingsData.status
            patientName.text = doctorBookingsData.fullName
            number.text = doctorBookingsData.number
            email.text = doctorBookingsData.email
            online.text = doctorBookingsData.online
            time.text = doctorBookingsData.time
            date.text = doctorBookingsData.date
            bookingsID.text = doctorBookingsData.bookingsID
            patientID.text = doctorBookingsData.patientId


            AcceptBTN.setOnClickListener {
                // Handle Accept Button Click
                val patientId = doctorBookingsData.patientId ?: ""
                val loadingDialog = showLoadingDialog(itemView.context)
                listener.onUpdateBookingStatus(patientId, "Accepted", loadingDialog)
            }

            RejectBTN.setOnClickListener {
                // Handle Reject Button Click
                val patientId = doctorBookingsData.patientId ?: ""
                val loadingDialog = showLoadingDialog(itemView.context)
                listener.onUpdateBookingStatus(patientId, "Rejected", loadingDialog)
            }

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