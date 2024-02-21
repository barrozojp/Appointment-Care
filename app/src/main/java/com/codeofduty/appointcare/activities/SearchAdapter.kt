package com.codeofduty.appointcare.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codeofduty.appointcare.R

class SearchAdapter(var mList: List<SearchData>) :
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

        }

        fun bind(searchData: SearchData) {
            logo.setImageResource(searchData.logo)
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
