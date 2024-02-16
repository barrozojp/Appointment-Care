package com.codeofduty.appointcare.activities

import android.animation.LayoutTransition
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.codeofduty.appointcare.R


class ServicesFragment : Fragment() {

    private lateinit var Service1Details: TextView
    private lateinit var Service2Details: TextView
    private lateinit var Service3Details: TextView
    private lateinit var Service4Details: TextView
    private lateinit var Service5Details: TextView
    private lateinit var Service6Details: TextView
    private lateinit var layout: LinearLayout
    private lateinit var expand: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_services, container, false)

        //Service NUMBER 1
        Service1Details = view.findViewById(R.id.Service1Details)
        layout = view.findViewById(R.id.layoutsService1)
        expand = view.findViewById(R.id.expandableService1)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (Service1Details.visibility == View.GONE) View.VISIBLE else View.GONE
            Service1Details.visibility = v
        }

        //Service NUMBER 2
        Service2Details = view.findViewById(R.id.Service2Details)
        layout = view.findViewById(R.id.layoutsService2)
        expand = view.findViewById(R.id.expandableService2)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (Service2Details.visibility == View.GONE) View.VISIBLE else View.GONE
            Service2Details.visibility = v
        }

        //Service NUMBER 3
        Service3Details = view.findViewById(R.id.Service3Details)
        layout = view.findViewById(R.id.layoutsService3)
        expand = view.findViewById(R.id.expandableService3)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (Service3Details.visibility == View.GONE) View.VISIBLE else View.GONE
            Service3Details.visibility = v
        }

        //Service NUMBER 4
        Service4Details = view.findViewById(R.id.Service4Details)
        layout = view.findViewById(R.id.layoutsService4)
        expand = view.findViewById(R.id.expandableService4)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (Service4Details.visibility == View.GONE) View.VISIBLE else View.GONE
            Service4Details.visibility = v
        }

        //Service NUMBER 5
        Service5Details = view.findViewById(R.id.Service5Details)
        layout = view.findViewById(R.id.layoutsService5)
        expand = view.findViewById(R.id.expandableService5)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (Service5Details.visibility == View.GONE) View.VISIBLE else View.GONE
            Service5Details.visibility = v
        }

        //Service NUMBER 6
        Service6Details = view.findViewById(R.id.Service6Details)
        layout = view.findViewById(R.id.layoutsService6)
        expand = view.findViewById(R.id.expandableService6)

        layout.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        expand.setOnClickListener {
            val v= if (Service6Details.visibility == View.GONE) View.VISIBLE else View.GONE
            Service6Details.visibility = v
        }

        return view
    }
}