package com.codeofduty.appointcare.activities

import android.animation.LayoutTransition
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.models.UserX

class HomeFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

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


        // Retrieve user data from SharedPreferences
        val userData = getUserData()

        // Set the text of tv_nameUser TextView
        val tvNameUser: TextView = view.findViewById(R.id.tv_nameUser)
        userData?.let { user ->
            val fullName = "Hello, ${user.Fname} ${user.Lname} !"
            tvNameUser.text = fullName
        }



        val visitWebTextView: TextView = view.findViewById(R.id.visitWEB)

        // Set OnClickListener to navigate to the website
        visitWebTextView.setOnClickListener {
            // Define the URL
            val url = "https://appointcare-web.netlify.app/"

            // Create an intent to open the URL in a browser
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            // Start the intent
            startActivity(intent)
        }

        val learnMoreButton: Button = view.findViewById(R.id.btn_learnMoreServices)

        // Set OnClickListener to navigate to ServicesFragment
        learnMoreButton.setOnClickListener {
            // Navigate to ServicesFragment
            val fragment = MyBookingsFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return view
    }
    // Retrieve user data from SharedPreferences
    private fun getUserData(): UserX? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val fname = sharedPreferences.getString("fname", null)
        val lname = sharedPreferences.getString("lname", null)
        // Retrieve other user data as needed

        return if (fname != null && lname != null) {
            UserX(Fname = fname, Lname = lname)
        } else {
            null
        }
    }
}
