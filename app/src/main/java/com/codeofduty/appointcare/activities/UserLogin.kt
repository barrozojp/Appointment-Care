package com.codeofduty.appointcare.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.databinding.ActivityUserLogInBinding
import com.codeofduty.appointcare.models.UserLoginResponse
import com.codeofduty.appointcare.models.LoginUser
import com.codeofduty.appointcare.models.UserX
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("CheckResult")
class UserLogin : AppCompatActivity() {

    private lateinit var binding: ActivityUserLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //EMAIL VALIDATION

        val EMAILStream = RxTextView.textChanges(binding.emailLOGINEditText)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        EMAILStream.subscribe{
            showEMAILExistAlert(it)
        }

        //PASSWORD VALIDATION
        val passwordStream = RxTextView.textChanges(binding.PasswordLOGINEditText)
            .skipInitialValue()
            .map { password ->
                password.length <8
            }
        passwordStream.subscribe{
            showPasswordAlert(it)
        }

        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            EMAILStream,
            passwordStream,
            { emailInvalid: Boolean, passwordInvalid: Boolean ->
                !emailInvalid && !passwordInvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnDoctorLogIn.isEnabled = true
                binding.btnDoctorLogIn.backgroundTintList = ContextCompat.getColorStateList(this,
                    R.color.primarycolor
                )
            } else {
                binding.btnDoctorLogIn.isEnabled = false
                binding.btnDoctorLogIn.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }

// Handle login button click
        binding.btnDoctorLogIn.setOnClickListener {
            // Show "Please wait" message
            Toast.makeText(this@UserLogin, "Please wait...", Toast.LENGTH_SHORT).show()

            // Call the login function
            loginUser()
        }

        binding.registerTV.setOnClickListener{
            startActivity(Intent(this, UserLogin::class.java))
        }
    }

    // EMAIL ALERT
    private fun showEMAILExistAlert(isNotValid: Boolean){
        binding.emailLOGINEditText.error = if (isNotValid) "Invalid eMAIL" else null
    }
    // PASSWORD ALERT
    private fun showPasswordAlert(isNotValid: Boolean) {
        binding.PasswordLOGINEditText.error = if (isNotValid) {
            binding.PasswordLOGINLayout.endIconMode = TextInputLayout.END_ICON_NONE
            "Password should be minimum of 8 characters"
        } else {
            binding.PasswordLOGINLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            null
        }
    }
    // Method to handle user login
    private fun loginUser() {
        // Show loading dialog
        val loadingDialog = createLoadingDialog()
        loadingDialog.show()

        val email = binding.emailLOGINEditText.text.toString().trim()
        val password = binding.PasswordLOGINEditText.text.toString().trim()

        val call = RetrofitClient.getService().signinUser(LoginUser(email = email, password = password))
        call.enqueue(object : Callback<UserLoginResponse> {
            override fun onResponse(call: Call<UserLoginResponse>, response: Response<UserLoginResponse>) {
                // Dismiss loading dialog
                loadingDialog.dismiss()

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let { login ->
                        handleSuccessfulLogin(login)
                    } ?: showError("Login failed. Please try again.")
                } else {
                    showError("Login failed. Please check your credentials.")
                }
            }

            override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                // Dismiss loading dialog
                loadingDialog.dismiss()

                showError("Failed to connect to server. Please try again later.")
            }
        })
    }
    // Function to create the loading dialog
    private fun createLoadingDialog(): AlertDialog {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false) // Prevent dialog dismissal on outside touch or back press

        val alertDialog = builder.create()

        alertDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_loading_background)


        return alertDialog


    }


    private fun handleSuccessfulLogin(login: UserLoginResponse) {
        // Save the token securely
        RetrofitClient.saveToken(this@UserLogin, login.token)

        // Save user data
        saveUserData(login.user)

        // Redirect based on user's role
        val targetActivity = when (login.user.role) {
            "Doctor" -> MainActivity::class.java
            "Patient" -> MainActivityPatient::class.java
            else -> SignInRegister::class.java // Handle other roles or no role specified
        }
        // Navigate to the appropriate activity
        startActivity(Intent(this@UserLogin, targetActivity))
        finish()
        Toast.makeText(
            this@UserLogin,
            "Login successful!",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun saveUserData(user: UserX) {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("fname", user.Fname)
        editor.putString("lname", user.Lname)
        editor.putString("_id", user._id)
        editor.putInt("age", user.age ?: -1)
        editor.putString("barangay", user.barangay)
        editor.putInt("consultPrice", user.consultPrice ?: -1)
        editor.putString("email", user.email)
        editor.putBoolean("f2f", user.f2f ?: false)
        editor.putString("gender", user.gender)
        editor.putInt("hn", user.hn ?: -1)
        editor.putInt("md", user.md ?: -1)
        editor.putString("municipality", user.municipality)
        editor.putString("number", user.number)
        editor.putBoolean("online", user.online ?: false)
        editor.putString("password", user.password)
        editor.putString("province", user.province)
        editor.putString("role", user.role)
        editor.putString("specialty", user.specialty)
        editor.putString("status", user.status)
        editor.putString("consultation", user.consultation)
        editor.apply()
    }





    private fun showError(message: String) {
        Toast.makeText(
            this@UserLogin,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

}