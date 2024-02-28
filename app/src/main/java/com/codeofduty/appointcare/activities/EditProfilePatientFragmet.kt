package com.codeofduty.appointcare.activities

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient
import com.jakewharton.rxbinding2.widget.RxTextView
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.codeofduty.appointcare.databinding.FragmentEditProfilePatientBinding

class EditProfilePatientFragmet : Fragment() {

    private lateinit var binding: FragmentEditProfilePatientBinding

    private lateinit var FNameEditText: EditText
    private lateinit var LnameEditText: EditText
    private lateinit var AgeEditText: EditText
    private lateinit var GenderEditText: EditText
    private lateinit var NumberEditText: EditText
    private lateinit var emailEditText: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfilePatientBinding.inflate(inflater, container, false)

        // Initialize EditText fields
        FNameEditText = binding.root.findViewById(R.id.FNameEditText)
        LnameEditText = binding.root.findViewById(R.id.LnameEditText)
        AgeEditText = binding.root.findViewById(R.id.AgeEditText)
        GenderEditText = binding.root.findViewById(R.id.GenderEditText)
        NumberEditText = binding.root.findViewById(R.id.NumberEditText)
        emailEditText = binding.root.findViewById(R.id.emailEditText)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve user data from SharedPreferences
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val fname = sharedPreferences.getString("fname", null)
        val lname = sharedPreferences.getString("lname", null)
        val age = sharedPreferences.getInt("age", -1)
        val gender = sharedPreferences.getString("gender", null)
        val number = sharedPreferences.getString("number", null)
        val email = sharedPreferences.getString("email", null)

        // Set the text of EditText fields with the retrieved user data
        FNameEditText.setText(fname)
        LnameEditText.setText(lname)
        AgeEditText.setText(age.toString())
        GenderEditText.setText(gender)
        NumberEditText.setText(number)
        emailEditText.setText(email)

        // Add this code where you handle the save button click event
        binding.btnSave.setOnClickListener {
            // Build an AlertDialog to confirm saving changes
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Save Changes")
            builder.setMessage("Are you sure you want to save the changes?")

            // Add the buttons
            builder.setPositiveButton("Yes") { dialog, which ->
                val updatedUser = UserX(
                    Fname = FNameEditText.text.toString(),
                    Lname = LnameEditText.text.toString(),
                    age = AgeEditText.text.toString().toIntOrNull(), // Convert to Int, handle null
                    gender = GenderEditText.text.toString(),
                    number = NumberEditText.text.toString(),
                    email = emailEditText.text.toString()
                )

                // Retrieve user ID from SharedPreferences
                val sharedPreferences =
                    requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("_id", null)

                userId?.let { id ->
                    updatePatientProfile(id, updatedUser)
                    // Navigate back to ProfileFragment after saving
                }
            }
            builder.setNegativeButton("No") { dialog, which ->
                // Do nothing, just dismiss the dialog
                dialog.dismiss()
            }

            val dialog = builder.create()

            dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

            dialog.setOnShowListener {
                val positiveButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
                val negativeButton = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE)
                positiveButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.reddish
                    )
                )
                negativeButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.neongreen
                    )
                )
            }

            dialog.show()

        }

        binding.btnCancel.setOnClickListener {
            // Navigate back to ProfileFragment
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun updatePatientProfile(userId: String, user: UserX) {
        val call = RetrofitClient.getService().updatePatientProfile(userId, user)
        call.enqueue(object : Callback<UserX> {
            override fun onResponse(call: Call<UserX>, response: Response<UserX>) {
                if (response.isSuccessful) {
                    // Handle successful update
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()

                    // Update SharedPreferences with the new user data
                    updateSharedPreferences(user)
                } else {
                    // Handle unsuccessful update
                    Toast.makeText(requireContext(), "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserX>, t: Throwable) {
                // Handle failure
                Toast.makeText(requireContext(), "Failed to connect to server. Please try again later.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateSharedPreferences(user: UserX) {
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Update SharedPreferences with the new user data
        editor.putString("fname", user.Fname)
        editor.putString("lname", user.Lname)
        editor.putInt("age", user.age ?: -1) // Use -1 if age is null
        editor.putString("gender", user.gender)
        editor.putString("number", user.number)
        editor.putString("email", user.email)

        // Commit the changes
        editor.apply()
    }

}
