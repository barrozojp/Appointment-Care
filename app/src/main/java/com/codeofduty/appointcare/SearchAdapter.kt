package com.codeofduty.appointcare

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(var mList: List<SearchData>) :
    RecyclerView.Adapter<SearchAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.logoIv)
        val titleTv: TextView = itemView.findViewById(R.id.titleTv)
        val searchDesc: TextView = itemView.findViewById(R.id.langDesc)

        init {
            searchDesc.visibility = View.GONE
        }

        fun bind(searchData: SearchData) {
            logo.setImageResource(searchData.logo)
            titleTv.text = searchData.title
            searchDesc.text = searchData.desc
            searchDesc.visibility = if (searchData.isExpandable) View.VISIBLE else View.GONE

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
