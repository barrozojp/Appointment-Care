package com.codeofduty.appointcare

import android.animation.LayoutTransition
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView


class FAQsFragment : Fragment() {

    private lateinit var FAQ1Details:TextView
    private lateinit var FAQ2Details:TextView
    private lateinit var FAQ3Details:TextView
    private lateinit var FAQ4Details:TextView
    private lateinit var FAQ5Details:TextView
    private lateinit var layout:LinearLayout
    private lateinit var expand:CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_faqs, container, false)

        //FAQ NUMBER 1
        FAQ1Details = view.findViewById(R.id.FAQ1Details)
        layout = view.findViewById(R.id.layoutsFAQ1)
        expand = view.findViewById(R.id.expandableFAQ1)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (FAQ1Details.visibility == View.GONE) View.VISIBLE else View.GONE
            FAQ1Details.visibility = v
        }

        //FAQ NUMBER TWO
        FAQ2Details = view.findViewById(R.id.FAQ2Details)
        layout = view.findViewById(R.id.layoutsFAQ2)
        expand = view.findViewById(R.id.expandableFAQ2)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (FAQ2Details.visibility == View.GONE) View.VISIBLE else View.GONE
            FAQ2Details.visibility = v
        }

        //FAQ NUMBER THREE
        FAQ3Details = view.findViewById(R.id.FAQ3Details)
        layout = view.findViewById(R.id.layoutsFAQ3)
        expand = view.findViewById(R.id.expandableFAQ3)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (FAQ3Details.visibility == View.GONE) View.VISIBLE else View.GONE
            FAQ3Details.visibility = v
        }

        //FAQ NUMBER FOUR
        FAQ4Details = view.findViewById(R.id.FAQ4Details)
        layout = view.findViewById(R.id.layoutsFAQ4)
        expand = view.findViewById(R.id.expandableFAQ4)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (FAQ4Details.visibility == View.GONE) View.VISIBLE else View.GONE
            FAQ4Details.visibility = v
        }

        //FAQ NUMBER FIVE
        FAQ5Details = view.findViewById(R.id.FAQ5Details)
        layout = view.findViewById(R.id.layoutsFAQ5)
        expand = view.findViewById(R.id.expandableFAQ5)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (FAQ5Details.visibility == View.GONE) View.VISIBLE else View.GONE
            FAQ5Details.visibility = v
        }

        return view
    }

}