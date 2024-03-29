package com.codeofduty.appointcare.activities

import SearchFragment
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codeofduty.appointcare.R

class SearchAdapter(var mList: List<SearchData>,
                    private val appointmentClickListener: AppointmentClickListener) :
    RecyclerView.Adapter<SearchAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.logoIv)
        val titleTv: TextView = itemView.findViewById(R.id.titleTv)
        val speciality: TextView = itemView.findViewById(R.id.specialityTv)
        val searchNum: TextView = itemView.findViewById(R.id.phonenum)
        val searchemail: TextView = itemView.findViewById(R.id.email)
        val searchlocation: TextView = itemView.findViewById(R.id.location)
        val cnsltPrice: TextView = itemView.findViewById(R.id.cnsltPrice)
        val viewProfileBTN: Button = itemView.findViewById(R.id.viewProfileBTN)
        val appointmentBTN: Button = itemView.findViewById(R.id.appointmentBTN)


        init {
            searchNum.visibility = View.GONE
            searchemail.visibility = View.GONE
            searchlocation.visibility = View.GONE
            cnsltPrice.visibility = View.GONE


            appointmentBTN.setOnClickListener {
                // Start MakeAppointment fragment using fragment's context
                val fragment = MakeAppointment()
                val fragmentManager = (itemView.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

        }

        fun bind(searchData: SearchData) {
            // Load image using Glide
            Glide.with(itemView)
                .load(searchData.imageData) // Use the imageData URL directly
                .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image
                .into(logo)
            titleTv.text = searchData.title
            speciality.text = searchData.specialty
            searchNum.text = searchData.num
            searchemail.text = searchData.email
            searchlocation.text = searchData.location
            cnsltPrice.text = searchData.cnsltPrice
            searchNum.visibility = if (searchData.isExpandable) View.VISIBLE else View.GONE
            searchemail.visibility = if (searchData.isExpandable) View.VISIBLE else View.GONE
            searchlocation.visibility = if (searchData.isExpandable) View.VISIBLE else View.GONE
            cnsltPrice.visibility = if (searchData.isExpandable) View.VISIBLE else View.GONE
            viewProfileBTN.visibility = if (searchData.isExpandable) View.VISIBLE else View.GONE
            appointmentBTN.visibility = if (searchData.isExpandable) View.VISIBLE else View.GONE

            appointmentBTN.setOnClickListener {
                // Pass the doctor's _id to the fragment when appointment button is clicked
                appointmentClickListener.onAppointmentClick(searchData._id)
            }

            itemView.setOnClickListener {
                val expanded = searchData.isExpandable
                searchData.isExpandable = !expanded
                notifyItemChanged(adapterPosition)
            }
        }

    }

    fun setFilteredList(mList: List<SearchData>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}
