package com.codeofduty.appointcare.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient.Companion.logout
import com.codeofduty.appointcare.models.UserX

class ProfileFragment : Fragment() {

    private lateinit var logoutButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve user data from SharedPreferences
        val userData = getUserData()

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val tvYourName: TextView = view.findViewById(R.id.tv_yourName)
        val tvYourEmail: TextView = view.findViewById(R.id.tv_yourEmail)
        val tvYourNumber: TextView = view.findViewById(R.id.tv_yourNumber)

        // Set the text of TextViews
        userData?.let { user ->
            val fullName = "${user.Fname} ${user.Lname}"
            tvYourName.text = fullName
            tvYourEmail.text = user.email
            tvYourNumber.text = user.number
        }

        logoutButton = view.findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            // Call the logout function
            logout(requireContext())
            // Show a toast message indicating successful logout
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
            // Start SignInRegisterActivity and finish current activity
            startActivity(Intent(requireContext(), SignInRegister::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        return view
    }
    // Retrieve user data from SharedPreferences
    private fun getUserData(): UserX? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val fname = sharedPreferences.getString("fname", null)
        val lname = sharedPreferences.getString("lname", null)
        val email = sharedPreferences.getString("email", null)
        val number = sharedPreferences.getString("number", null)
        // Retrieve other user data as needed

        return if (fname != null && lname != null && email != null && number != null) {
            UserX(Fname = fname, Lname = lname, email = email, number = number)
        } else {
            null
        }
    }
}
