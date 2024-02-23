    package com.codeofduty.appointcare.activities

    import User
    import android.annotation.SuppressLint
    import android.content.Context
    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Patterns
    import android.view.View
    import android.widget.Button
    import android.widget.Toast
    import androidx.core.content.ContextCompat
    import com.codeofduty.appointcare.R
    import com.codeofduty.appointcare.api.RetrofitClient
    import com.codeofduty.appointcare.databinding.ActivityPatientLogInBinding
    import com.codeofduty.appointcare.models.LoginResponse
    import com.codeofduty.appointcare.models.LoginUser
    import com.google.android.material.textfield.TextInputLayout
    import com.jakewharton.rxbinding2.widget.RxTextView
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response


    @SuppressLint("CheckResult")
    class PatientLogIn : AppCompatActivity() {

        private lateinit var binding: ActivityPatientLogInBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityPatientLogInBinding.inflate(layoutInflater)
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
                    binding.btnPatientLogIn.isEnabled = true
                    binding.btnPatientLogIn.backgroundTintList = ContextCompat.getColorStateList(this,
                        R.color.primarycolor
                    )
                } else {
                    binding.btnPatientLogIn.isEnabled = false
                    binding.btnPatientLogIn.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
                }
            }


            val btnLogIn: Button = findViewById(R.id.btn_PatientLogIn)


    // Handle login button click
            binding.btnPatientLogIn.setOnClickListener {
                // Show "Please wait" message
                Toast.makeText(this@PatientLogIn, "Please wait...", Toast.LENGTH_SHORT).show()

                // Call the login function
                loginUser()
            }

            binding.registerTV.setOnClickListener{
                startActivity(Intent(this, LogIn::class.java))
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
        private fun loginUser() {
            val email = binding.emailLOGINEditText.text.toString().trim()
            val password = binding.PasswordLOGINEditText.text.toString().trim()

            val call = RetrofitClient.getService().signinUser(LoginUser(role = "Doctor", email = email, password = password))
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.let { login ->
                            handleSuccessfulLogin(login)
                        } ?: showError("Login failed. Please try again.")
                    } else {
                        showError("Login failed. Please check your credentials.")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showError("Failed to connect to server. Please try again later.")
                }
            })
        }

        private fun handleSuccessfulLogin(login: LoginResponse) {
            // Save the token securely
            RetrofitClient.saveToken(this@PatientLogIn, login.token)

            // Login successful, navigate to main activity
            startActivity(Intent(this@PatientLogIn, MainActivityPatient::class.java))
            finish()
            Toast.makeText(
                this@PatientLogIn,
                "Login successful!",
                Toast.LENGTH_SHORT
            ).show()
        }

        private fun saveUserData(user: User) {
            val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("fname", user.Fname)
            editor.putString("lname", user.Lname)
            // Save other user data as needed
            editor.apply()
        }
        private fun showError(message: String) {
            Toast.makeText(
                this@PatientLogIn,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }