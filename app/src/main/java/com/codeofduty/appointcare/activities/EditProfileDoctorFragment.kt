package com.codeofduty.appointcare.activities

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.models.UserX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.codeofduty.appointcare.databinding.FragmentEditProfileDoctorBinding
import com.codeofduty.appointcare.models.ChangePassword

class EditProfileDoctorFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileDoctorBinding

    private lateinit var FNameEditText: EditText
    private lateinit var LnameEditText: EditText
    private lateinit var AgeEditText: EditText
    private lateinit var GenderEditText: EditText
    private lateinit var NumberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var mdYEAREditText: EditText
    private lateinit var specialtyEditText: EditText
    private lateinit var hnEditText: EditText
    private lateinit var barangayEditText: EditText
    private lateinit var municipalityEditText: EditText
    private lateinit var provinceEditText: EditText
    private lateinit var cnsltPriceEditText: EditText

    //FOR CHANGE PASSWORD
    private lateinit var CurrentPassEditText: EditText
    private lateinit var NewPassEditText: EditText
    private lateinit var ConfNewPassEditText: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using data binding
        binding = FragmentEditProfileDoctorBinding.inflate(inflater, container, false)

        // Initialize EditText fields
        FNameEditText = binding.root.findViewById(R.id.FNameEditText)
        LnameEditText = binding.root.findViewById(R.id.LnameEditText)
        AgeEditText = binding.root.findViewById(R.id.AgeEditText)
        GenderEditText = binding.root.findViewById(R.id.GenderEditText)
        NumberEditText = binding.root.findViewById(R.id.NumberEditText)
        emailEditText = binding.root.findViewById(R.id.emailEditText)
        mdYEAREditText = binding.root.findViewById(R.id.mdYEAREditText)
        specialtyEditText = binding.root.findViewById(R.id.specialtyEditText)
        hnEditText = binding.root.findViewById(R.id.hnEditText)
        barangayEditText = binding.root.findViewById(R.id.barangayEditText)
        municipalityEditText = binding.root.findViewById(R.id.municipalityEditText)
        provinceEditText = binding.root.findViewById(R.id.provinceEditText)
        cnsltPriceEditText = binding.root.findViewById(R.id.cnsltPriceEditText)


        //FOR CHANGE PASSWORD
        CurrentPassEditText = binding.root.findViewById(R.id.CurrentPassEditText)
        NewPassEditText = binding.root.findViewById(R.id.NewPassEditText)
        ConfNewPassEditText = binding.root.findViewById(R.id.ConfNewPassEditText)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve user data from SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val fname = sharedPreferences.getString("fname", null)
        val lname = sharedPreferences.getString("lname", null)
        val age = sharedPreferences.getInt("age", -1)
        val gender = sharedPreferences.getString("gender", null)
        val number = sharedPreferences.getString("number", null)
        val email = sharedPreferences.getString("email", null)
        val md = sharedPreferences.getInt("md", -1)
        val specialty = sharedPreferences.getString("specialty", null)
        val hn = sharedPreferences.getInt("hn", -1)
        val barangay = sharedPreferences.getString("barangay", null)
        val municipality = sharedPreferences.getString("municipality", null)
        val province = sharedPreferences.getString("province", null)
        val consultPrice = sharedPreferences.getInt("consultPrice", -1)







        // Set the text of EditText fields with the retrieved user data
        FNameEditText.setText(fname)
        LnameEditText.setText(lname)
        AgeEditText.setText(age.toString())
        GenderEditText.setText(gender)
        NumberEditText.setText(number)
        emailEditText.setText(email)
        mdYEAREditText.setText(md.toString())
        specialtyEditText.setText(specialty)
        hnEditText.setText(hn.toString())
        barangayEditText.setText(barangay)
        municipalityEditText.setText(municipality)
        provinceEditText.setText(province)
        cnsltPriceEditText.setText(consultPrice.toString())






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
                    email = emailEditText.text.toString(),
                    md = mdYEAREditText.text.toString().toIntOrNull(),
                    specialty = specialtyEditText.text.toString(),
                    hn = hnEditText.text.toString().toIntOrNull(),
                    barangay = barangayEditText.text.toString(),
                    municipality = municipalityEditText.text.toString(),
                    province = provinceEditText.text.toString(),
                    consultPrice = cnsltPriceEditText.text.toString().toIntOrNull()

                )

                // Retrieve user ID from SharedPreferences
                val sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("_id", null)

                userId?.let { id ->
                    updateDoctorProfile(id, updatedUser)
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
                positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.reddish))
                negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.neongreen))
            }

            dialog.show()

        }


        binding.btnCancel.setOnClickListener {
            // Navigate back to ProfileFragment
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.btnSavePass.setOnClickListener {
            // Show loading dialog
            val loadingDialog = createLoadingDialog()
            loadingDialog.show()

            // Build an AlertDialog to confirm saving changes
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Change Password")
            builder.setMessage("Are you sure you want to change your password?")

            // Add the buttons
            builder.setPositiveButton("Yes") { dialog, which ->
                val currentPassword = CurrentPassEditText.text.toString()
                val newPassword = NewPassEditText.text.toString()
                val confirmNewPassword = ConfNewPassEditText.text.toString()

                if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    // Display error message if any field is empty
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else if (newPassword != confirmNewPassword) {
                    // Display error message if new password and confirm new password do not match
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), "New password and confirm new password must match", Toast.LENGTH_SHORT).show()
                } else {
                    // Call the changePassword function to update the password
                    val userId = sharedPreferences.getString("_id", null) // Assuming you retrieve the user ID from SharedPreferences
                    val changePassword = ChangePassword(currentPassword, newPassword)

                    userId?.let { id ->
                        // Call the Retrofit service to change the password
                        val call = RetrofitClient.getService().changePassword(id, changePassword)
                        call.enqueue(object : Callback<Any> {
                            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                                if (response.isSuccessful) {
                                    loadingDialog.dismiss()
                                    // Password changed successfully
                                    Toast.makeText(requireContext(), "Password changed successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Handle unsuccessful response
                                    Toast.makeText(requireContext(), "Failed to change password. Please try again.", Toast.LENGTH_SHORT).show()
                                    loadingDialog.dismiss()

                                }
                            }

                            override fun onFailure(call: Call<Any>, t: Throwable) {
                                // Handle failure
                                loadingDialog.dismiss()
                                Toast.makeText(requireContext(), "Failed to connect to server. Please try again later.", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
            builder.setNegativeButton("No") { dialog, which ->
                // Do nothing, just dismiss the dialog
                dialog.dismiss()
                loadingDialog.dismiss()
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

        binding.btnCancelPass.setOnClickListener {
            // Navigate back to ProfileFragment
            requireActivity().supportFragmentManager.popBackStack()
        }


    }
    private fun createLoadingDialog(): AlertDialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false) // Prevent dialog dismissal on outside touch or back press

        val alertDialog = builder.create()

        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_loading_background)

        return alertDialog
    }


    private fun updateDoctorProfile(userId: String, user: UserX) {
        // Show loading dialog
        val loadingDialog = createLoadingDialog()
        loadingDialog.show()

        val call = RetrofitClient.getService().updateDoctorProfile(userId, user)
        call.enqueue(object : Callback<UserX> {
            override fun onResponse(call: Call<UserX>, response: Response<UserX>) {
                if (response.isSuccessful) {
                    loadingDialog.dismiss()

                    // Handle successful update
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()

                    // Update SharedPreferences with the new user data
                    updateSharedPreferences(user)
                } else {
                    // Handle unsuccessful update
                    Toast.makeText(requireContext(), "Failed to update profile. Please try again.", Toast.LENGTH_SHORT).show()
                    loadingDialog.dismiss()

                }
            }

            override fun onFailure(call: Call<UserX>, t: Throwable) {
                // Handle failure
                loadingDialog.dismiss()
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
        editor.putInt("md", user.md ?: -1)
        editor.putString("specialty", user.specialty)
        editor.putInt("hn", user.hn ?: -1)
        editor.putString("barangay", user.barangay)
        editor.putString("municipality", user.municipality)
        editor.putString("province", user.province)
        editor.putInt("consultPrice", user.consultPrice ?: -1)





        // Commit the changes
        editor.apply()
    }

}

