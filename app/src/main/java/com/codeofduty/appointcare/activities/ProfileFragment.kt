package com.codeofduty.appointcare.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient.Companion.logout

class ProfileFragment : Fragment() {

    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

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
}
