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

class RejectedAdapter(var mListRejected: List<RejectedBookingsData>,private val listener: DoctorRejectedPatientsFragment) :
        RecyclerView.Adapter<RejectedAdapter.RejectedViewHolder>() {

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

    inner class RejectedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        val recoverBTN: Button = itemView.findViewById(R.id.recoverBTN)
        val deleteBTN: Button = itemView.findViewById(R.id.deleteBTN)


        fun bind(rejectedBookingsData: RejectedBookingsData) {

            statusBookings.text = rejectedBookingsData.status
            patientName.text = rejectedBookingsData.fullName
            number.text = rejectedBookingsData.number
            email.text = rejectedBookingsData.email
            online.text = rejectedBookingsData.online
            time.text = rejectedBookingsData.time
            date.text = rejectedBookingsData.date
            bookingsID.text = rejectedBookingsData.bookingsID


            recoverBTN.setOnClickListener {
                // Handle Accept Button Click
                val patientId = rejectedBookingsData.patientId ?: ""
                val loadingDialog = showLoadingDialog(itemView.context)
                listener.onUpdateBookingStatus(patientId, "Pending", loadingDialog)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RejectedAdapter.RejectedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rejected_appoints_items, parent, false)
        return RejectedViewHolder(view)
    }
    override fun onBindViewHolder(holder: RejectedAdapter.RejectedViewHolder, position: Int) {
        holder.bind(mListRejected[position])
    }

    override fun getItemCount(): Int {
        return mListRejected.size
    }
}