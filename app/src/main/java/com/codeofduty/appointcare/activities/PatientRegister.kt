package com.codeofduty.appointcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.databinding.ActivityPatientRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import patientRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatientRegister : AppCompatActivity() {
    private lateinit var binding: ActivityPatientRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //First Name Validation//

        val FNameStream = RxTextView.textChanges(binding.FNameRegEditText)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        FNameStream.subscribe {
            showFNameExistAlert(it)
        }
        //Last Name Validation//

        val LNameStream = RxTextView.textChanges(binding.LNameRegEditText)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        LNameStream.subscribe {
            showLNameExistAlert(it)
        }

        //Phone Number Validation

        val phoneNumberStream = RxTextView.textChanges(binding.PhoneNumRegEditText)
            .skipInitialValue()
            .map { phoneNumber ->
                phoneNumber.length >= 11
            }
        phoneNumberStream.subscribe {
            showPhoneNumExistAlert(it)
        }

        //GENDER VALIDATION

        val genderStream = RxTextView.textChanges(binding.genderEditText)
            .skipInitialValue()
            .map { gender ->
                gender.toString() !in listOf("Male", "Female")
            }
        genderStream.subscribe {
            showgenderExistAlert(it)
        }


        //EMAIL VALIDATION

        val EMAILStream = RxTextView.textChanges(binding.emailRegEditText)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        EMAILStream.subscribe {
            showEMAILExistAlert(it)
        }
        //AGE Validation//

        val AgeStream = RxTextView.textChanges(binding.AgeRegEditText)
            .skipInitialValue()
            .map { age ->
                age.length > 2
            }
        AgeStream.subscribe {
            showAgeExistAlert(it)
        }

        //PASSWORD VALIDATION
        val passwordStream = RxTextView.textChanges(binding.PasswordRegEditText)
            .skipInitialValue()
            .map { password ->
                password.length < 8
            }
        passwordStream.subscribe {
            showPasswordAlert(it)
        }
        // CONFIRM PASSWORD VALIDATION
        val confirmPasswordStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.PasswordRegEditText)
                .skipInitialValue()
                .map { password ->
                    password.toString() != binding.ConfirmPasswordRegEditText.text.toString()
                },
            RxTextView.textChanges(binding.ConfirmPasswordRegEditText)
                .skipInitialValue()
                .map { confirmPassword ->
                    confirmPassword.toString() != binding.PasswordRegEditText.text.toString()
                })
        confirmPasswordStream.subscribe {
            showConfirmPasswordAlert(it)
        }

        //ENABLE BUTTON TRUE OR FALSE

        val invalidFieldStream = io.reactivex.Observable.combineLatest(
            FNameStream,
            LNameStream,
            phoneNumberStream,
            EMAILStream,
            AgeStream,
            passwordStream,
            confirmPasswordStream,
            { FnameInvalid: Boolean, LnameInvalid: Boolean, phonenumInvalid: Boolean, emailInvalid: Boolean, ageInvalid: Boolean, passwordInvalid: Boolean, confirmPassInvalid: Boolean ->
                !FnameInvalid && !LnameInvalid && !phonenumInvalid && !emailInvalid && !ageInvalid && !passwordInvalid && !confirmPassInvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(
                    this,
                    R.color.primarycolor
                )
            } else {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }


// CLICK
        binding.btnRegister.setOnClickListener {
            Toast.makeText(this@PatientRegister, "Please wait...", Toast.LENGTH_SHORT).show()

            registerDoctor()
        }
        binding.haveAccBTN.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
        }

    }
    private fun registerDoctor() {

        val FName = binding.FNameRegEditText.text.toString().trim()
        val LName = binding.LNameRegEditText.text.toString().trim()
        val phoneNumber = binding.PhoneNumRegEditText.text.toString().trim()
        val gender = binding.genderEditText.text.toString().trim()
        val email = binding.emailRegEditText.text.toString().trim()
        val age = binding.AgeRegEditText.text.toString().trim().toInt()
        val password = binding.PasswordRegEditText.text.toString().trim()

        val patientForm = patientRegister(
            role = "Patient",
            Fname = FName,
            Lname = LName,
            number = phoneNumber,
            gender = gender,
            age = age,
            email = email,
            password = password,
            consultation = " "
        )

        val call = RetrofitClient.getService().registerPatient(patientForm)
        call.enqueue(object : Callback<patientRegister> {
            override fun onResponse(call: Call<patientRegister>, response: Response<patientRegister>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PatientRegister, "Patient registered successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@PatientRegister, LogIn::class.java))
                    finish()
                } else {
                    Toast.makeText(this@PatientRegister, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<patientRegister>, t: Throwable) {
                Toast.makeText(this@PatientRegister, "Registration Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@PatientRegister, LogIn::class.java))
                finish()
            }
        })
    }


    private fun showToast(gender: String) {
        Toast.makeText(this, "You chose $gender", Toast.LENGTH_SHORT).show()
    }



// SHOW ALERTS!

    // FNAME ALERT
    private fun showFNameExistAlert(isNotValid: Boolean) {
        binding.FNameRegEditText.error = if (isNotValid) "Invalid First Name" else null
    }

    // LNAME ALERT
    private fun showLNameExistAlert(isNotValid: Boolean) {
        binding.LNameRegEditText.error = if (isNotValid) "Invalid Last Name" else null
    }

    // NUMBER ALERT
    private fun showPhoneNumExistAlert(isNotValid: Boolean) {
        binding.PhoneNumRegEditText.error = if (isNotValid) "Cannot exceed 10 characters" else null
    }

    // GENDER ALERT
    private fun showgenderExistAlert(isNotValid: Boolean) {
        binding.genderEditText.error = if (isNotValid) "Invalid Gender (Male or Female)" else null
    }

    // EMAIL ALERT
    private fun showEMAILExistAlert(isNotValid: Boolean) {
        binding.emailRegEditText.error = if (isNotValid) "Invalid eMAIL" else null
    }

    // AGE ALERT
    private fun showAgeExistAlert(isNotValid: Boolean) {
        binding.AgeRegEditText.error = if (isNotValid) "Invalid Age" else null
    }

    // PASSWORD ALERT
    private fun showPasswordAlert(isNotValid: Boolean) {
        binding.PasswordRegEditText.error = if (isNotValid) {
            binding.PasswordRegLayout.endIconMode = TextInputLayout.END_ICON_NONE
            "Password should be minimum of 8 characters"
        } else {
            binding.PasswordRegLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            null
        }
    }

    // CONFIRM PASSWORD ALERT
    private fun showConfirmPasswordAlert(isNotValid: Boolean) {
        binding.ConfirmPasswordRegEditText.error = if (isNotValid) {
            binding.ConfirmPasswordRegLayout.endIconMode = TextInputLayout.END_ICON_NONE
            "Password does not match"
        } else {
            binding.ConfirmPasswordRegLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            null
        }
    }
}