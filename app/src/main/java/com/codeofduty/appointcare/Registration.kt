package com.codeofduty.appointcare

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import com.codeofduty.appointcare.databinding.ActivityRegistrationBinding
import io.reactivex.Observable


@SuppressLint("CheckResult")
class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RADIO GROUP//
        val radioGroup = findViewById<RadioGroup>(R.id.gender)
        val maleRadioButton = findViewById<RadioButton>(R.id.gendermale)
        val femaleRadioButton = findViewById<RadioButton>(R.id.genderfemale)
        val radioGroupReg = findViewById<RadioGroup>(R.id.registerAS)
        val doctorRadioButton = findViewById<RadioButton>(R.id.regDOCTOR)
        val patientRadioButton = findViewById<RadioButton>(R.id.regPATIENT)

        // Set OnClickListener for male radio button
        maleRadioButton.setOnClickListener {
            if (femaleRadioButton.isChecked) {
                femaleRadioButton.isChecked = false
            }
            showToast("Male")
        }

        // Set OnClickListener for female radio button
        femaleRadioButton.setOnClickListener {
            if (maleRadioButton.isChecked) {
                maleRadioButton.isChecked = false
            }
            showToast("Female")
        }


        // Set OnClickListener for Doctor radio button
        doctorRadioButton.setOnClickListener {
            if (patientRadioButton.isChecked) {
                patientRadioButton.isChecked = false
            }
            showToast("Doctor")
        }

        // Set OnClickListener for Patient radio button
        patientRadioButton.setOnClickListener {
            if (doctorRadioButton.isChecked) {
                doctorRadioButton.isChecked = false
            }
            showToast("Patient")
        }

        // Register AS Validation
        val registerAsStream = Observable.create<Boolean> { emitter ->
            binding.registerAS.setOnCheckedChangeListener { group, checkedId ->
                val isValid = checkedId != -1
                emitter.onNext(isValid)
            }
        }
        registerAsStream.subscribe { isValid ->
            showRegisterAsAlert(!isValid)
        }

        //First Name Validation//

        val FNameStream = RxTextView.textChanges(binding.FNameRegEditText)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        FNameStream.subscribe{
            showFNameExistAlert(it)
        }
        //Last Name Validation//

        val LNameStream = RxTextView.textChanges(binding.LNameRegEditText)
            .skipInitialValue()
            .map { name ->
                name.isEmpty()
            }
        LNameStream.subscribe{
            showLNameExistAlert(it)
        }

        //Phone Number Validation

        val phoneNumberStream = RxTextView.textChanges(binding.PhoneNumRegEditText)
            .skipInitialValue()
            .map { phoneNumber ->
                phoneNumber.length >= 11
            }
        phoneNumberStream.subscribe{
            showPhoneNumExistAlert(it)
        }
        // Gender Validation
        val genderStream = Observable.create<Boolean> { emitter ->
            binding.gender.setOnCheckedChangeListener { group, checkedId ->
                val isValid = checkedId != -1
                emitter.onNext(isValid)
            }
        }
        genderStream.subscribe { isValid ->
            showGenderAlert(!isValid)
        }


        //EMAIL VALIDATION

        val EMAILStream = RxTextView.textChanges(binding.emailRegEditText)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        EMAILStream.subscribe{
            showEMAILExistAlert(it)
        }
        //AGE Validation//

        val AgeStream = RxTextView.textChanges(binding.AgeRegEditText)
            .skipInitialValue()
            .map { age ->
                age.isEmpty()
            }
        AgeStream.subscribe{
            showAgeExistAlert(it)
        }

        //PASSWORD VALIDATION
        val passwordStream = RxTextView.textChanges(binding.PasswordRegEditText)
            .skipInitialValue()
            .map { password ->
                password.length <8
            }
        passwordStream.subscribe{
            showPasswordAlert(it)
        }
        // CONFIRM PASSWORD VALIDATION
        val confirmPasswordStream = io.reactivex.Observable.merge(
            RxTextView.textChanges(binding.PasswordRegEditText)
                .skipInitialValue()
                .map { password ->
                    password.toString() !=binding.ConfirmPasswordRegEditText.text.toString()
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
                !FnameInvalid && !LnameInvalid && !phonenumInvalid && !emailInvalid &&! ageInvalid && !passwordInvalid && !confirmPassInvalid
            })
        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(this, R.color.primarycolor)
            } else {
                binding.btnRegister.isEnabled = false
                binding.btnRegister.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.darker_gray)
            }
        }


// CLICK
        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this, LogIn::class.java))
        }
        binding.haveAccBTN.setOnClickListener{
            startActivity(Intent(this, LogIn::class.java))
        }

    }
    private fun showToast(gender: String) {
        Toast.makeText(this, "You chose $gender", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(gender: String, registerAS: String) {
        Toast.makeText(this, "You chose $registerAS", Toast.LENGTH_SHORT).show()
    }


// SHOW ALERTS!

    // FNAME ALERT
    private fun showFNameExistAlert(isNotValid: Boolean){
        binding.FNameRegEditText.error = if (isNotValid) "Invalid First Name" else null
    }
    // LNAME ALERT
    private fun showLNameExistAlert(isNotValid: Boolean){
        binding.LNameRegEditText.error = if (isNotValid) "Invalid Last Name" else null
    }
    // NUMBER ALERT
    private fun showPhoneNumExistAlert(isNotValid: Boolean){
        binding.PhoneNumRegEditText.error = if (isNotValid) "Cannot exceed 10 characters" else null
    }
    // EMAIL ALERT
    private fun showEMAILExistAlert(isNotValid: Boolean){
        binding.emailRegEditText.error = if (isNotValid) "Invalid eMAIL" else null
    }
    // AGE ALERT
    private fun showAgeExistAlert(isNotValid: Boolean){
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
    // Show Register As Alert
    private fun showRegisterAsAlert(isNotValid: Boolean) {
        if (isNotValid) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
        }
    }

    // Show Gender Alert
    private fun showGenderAlert(isNotValid: Boolean) {
        if (isNotValid) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
        }
    }
}
