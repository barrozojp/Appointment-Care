package com.codeofduty.appointcare.activities

import User
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
import android.app.AlertDialog
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.api.RetrofitClient.Companion.logout
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var logoutButton: Button
    private lateinit var editProfileButton: Button
    private lateinit var profileImageView: ImageView



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
        val tvYourGender: TextView = view.findViewById(R.id.tv_yourGender)
        profileImageView = view.findViewById(R.id.profileIMG) // Initialize profileImageView


        // Set the text of TextViews
        userData?.let { user ->
            val fullName = "${user.Fname} ${user.Lname}"
            tvYourName.text = fullName
            tvYourEmail.text = user.email
            tvYourNumber.text = user.number
            tvYourGender.text = user.gender

            // Load profile image using image data from SharedPreferences
            loadProfileImage(user.imageData)
        }

        editProfileButton = view.findViewById(R.id.btn_editProfile)

        editProfileButton.setOnClickListener {
            // Retrieve user data from SharedPreferences
            val userData = getUserData()

            // Check if userData is not null and if it contains the role information
            if (userData != null && userData.role != null) {
                val role = userData.role

                // Check the user role and navigate accordingly
                if (role == "Doctor") {
                    // Navigate to EditProfileDoctorFragment
                    val fragment = EditProfileDoctorFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                } else if (role == "Patient") {
                    // Navigate to EditProfilePatientFragment
                    val fragment = EditProfilePatientFragmet()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    // Handle other roles or scenarios
                    // You can show a toast message or handle it as per your requirement
                    Toast.makeText(requireContext(), "Unsupported role", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle the case where userData or role is null
                // You can show a toast message or handle it as per your requirement
                Toast.makeText(requireContext(), "User data or role not found", Toast.LENGTH_SHORT).show()
            }
        }



        logoutButton = view.findViewById(R.id.btn_logout)
        logoutButton.setOnClickListener {
            // Create an AlertDialog.Builder
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Logout")
            builder.setMessage("Are you sure you want to log out?")

            // Add the buttons
            builder.setPositiveButton("Yes") { dialog, which ->
                // Call the logout function
                logout(requireContext())
                // Show a toast message indicating successful logout
                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                // Start SignInRegisterActivity and finish current activity
                startActivity(Intent(requireContext(), SignInRegister::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
            builder.setNegativeButton("No") { dialog, which ->
                // Do nothing, just dismiss the dialog
                dialog.dismiss()
            }

            // Create and show the AlertDialog
            val dialog = builder.create()
            // Set background drawable
            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

            // Set custom style to the buttons
            dialog.setOnShowListener {
                val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                val negativeButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
                positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.reddish))
                negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.neongreen))
            }


            dialog.show()
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
        val gender = sharedPreferences.getString("gender", null)
        val role = sharedPreferences.getString("role", null)
        val _id = sharedPreferences.getString("_id", null)
        val imageData = sharedPreferences.getString("imageData", null)

        // Retrieve other user data as needed

        return if (fname != null && lname != null && email != null && number != null && gender !=null && role !=null && _id !=null && imageData !=null) {
            UserX(Fname = fname, Lname = lname, email = email, number = number, gender = gender, role = role, _id = _id, imageData = imageData)
        } else {
            null
        }
    }
    // Function to load profile image using API call
    private fun loadProfileImage(imageData: String?) {
        imageData?.let {
            Glide.with(this@ProfileFragment)
                .load(imageData)
                .placeholder(R.drawable.blank_profile) // Placeholder image
                .error(R.drawable.blank_profile) // Error image
                .into(profileImageView)
        }
    }



}
