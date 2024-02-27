package com.codeofduty.appointcare.activities

import User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.api.RetrofitClient
import com.codeofduty.appointcare.databinding.ActivityDoctorRegisterBinding
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class DoctorRegister : AppCompatActivity() {
    private lateinit var binding: ActivityDoctorRegisterBinding
    private var f2fChecked = false
    private var onlineChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorRegisterBinding.inflate(layoutInflater)
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

        //SPECIALITY
        val specialityStream = RxTextView.textChanges(binding.specialityEditText)
            .skipInitialValue()
            .map { speciality ->
                speciality.isEmpty()
            }
        specialityStream.subscribe {
            showSpecialityExistAlert(it)
        }

        //MD YEAR
        val mdStream = RxTextView.textChanges(binding.mdEditText)
            .skipInitialValue()
            .map { md ->
                md.length == 5
            }
        mdStream.subscribe {
            showmdExistAlert(it)
        }
        //CONSTULTATION PRICE
        val cnsltStream = RxTextView.textChanges(binding.cnsltPriceEditText)
            .skipInitialValue()
            .map { cnslt ->
                cnslt.isEmpty()
            }
        cnsltStream.subscribe {
            cnsltExistAlert(it)
        }
        //PROVINCE
        val ProvinceStream = RxTextView.textChanges(binding.provinceEditText)
            .skipInitialValue()
            .map { province ->
                province.isEmpty()
            }
        ProvinceStream.subscribe {
            provinceExistAlert(it)
        }
        //MUNICIPALITY
        val municipalityStream = RxTextView.textChanges(binding.municipalityEditText)
            .skipInitialValue()
            .map { municipality ->
                municipality.isEmpty()
            }
        municipalityStream.subscribe {
            municipalityExistAlert(it)
        }
        //BARANGGAY
        val brngyStream = RxTextView.textChanges(binding.municipalityEditText)
            .skipInitialValue()
            .map { brngy ->
                brngy.isEmpty()
            }
        brngyStream.subscribe {
            brngyExistAlert(it)
        }

        //HOUSE NUMBER
        val hnStream = RxTextView.textChanges(binding.hnEditText)
            .skipInitialValue()
            .map { hn ->
                hn.isEmpty()
            }
        hnStream.subscribe {
            hnExistAlert(it)
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
            specialityStream,
            mdStream,
            passwordStream,
            confirmPasswordStream,
            { FnameInvalid: Boolean, LnameInvalid: Boolean, phonenumInvalid: Boolean, emailInvalid: Boolean, ageInvalid: Boolean, specialityInvalid: Boolean, mdInvalid: Boolean,  passwordInvalid: Boolean, confirmPassInvalid: Boolean ->
                !FnameInvalid && !LnameInvalid && !phonenumInvalid && !emailInvalid && !ageInvalid && !specialityInvalid && !mdInvalid &&  !passwordInvalid && !confirmPassInvalid
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

        val invalidFieldStream2 = io.reactivex.Observable.combineLatest(
            cnsltStream,
            ProvinceStream,
            municipalityStream,
            brngyStream,
            hnStream,
            { cnsltInvalid: Boolean, provinceInvalid: Boolean, municipalityInvalid: Boolean, brngyInvalid: Boolean, hnInvalid: Boolean ->
                !cnsltInvalid && !provinceInvalid && !municipalityInvalid && !brngyInvalid && !hnInvalid
            })
        invalidFieldStream2.subscribe { isValid ->
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
            Toast.makeText(this@DoctorRegister, "Please wait...", Toast.LENGTH_SHORT).show()

            registerDoctor()
        }
        binding.haveAccBTN.setOnClickListener {
            startActivity(Intent(this, UserLogin::class.java))
        }


        // Checkboxes setup
        binding.checkboxF2f.setOnCheckedChangeListener { _, isChecked ->
            f2fChecked = isChecked
        }

        binding.checkboxOnline.setOnCheckedChangeListener { _, isChecked ->
            onlineChecked = isChecked
        }


    }
    private fun registerDoctor() {
        val FName = binding.FNameRegEditText.text.toString().trim()
        val LName = binding.LNameRegEditText.text.toString().trim()
        val phoneNumber = binding.PhoneNumRegEditText.text.toString().trim()
        val gender = binding.genderEditText.text.toString().trim()
        val email = binding.emailRegEditText.text.toString().trim()
        val age = binding.AgeRegEditText.text.toString().trim().toInt()
        val specialty = binding.specialityEditText.text.toString().trim()
        val md = binding.mdEditText.text.toString().trim()
        val consultPrice = binding.cnsltPriceEditText.text.toString().trim()
        val province = binding.provinceEditText.text.toString().trim()
        val municipality = binding.municipalityEditText.text.toString().trim()
        val barangay = binding.brgyEditText.text.toString().trim()
        val hn = binding.hnEditText.text.toString().trim()
        val password = binding.PasswordRegEditText.text.toString().trim()

        val user = User(
            role = "Doctor",
            Fname = FName,
            Lname = LName,
            number = phoneNumber,
            gender = gender,
            age = age,
            specialty = specialty,
            md = md,
            consultPrice = consultPrice,
            province = province,
            municipality = municipality,
            barangay = barangay,
            hn = hn,
            email = email,
            password = password,
            f2f = f2fChecked,
            online = onlineChecked,
            status = "Pending"
        )

        val call = RetrofitClient.getService().registerDoctor(user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@DoctorRegister, "Doctor registered successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@DoctorRegister, UserLogin::class.java))
                    finish()
                } else {
                    Toast.makeText(this@DoctorRegister, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@DoctorRegister, "Registration Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@DoctorRegister, UserLogin::class.java))
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

    // SPECIALITY ALERT
    private fun showSpecialityExistAlert(isNotValid: Boolean) {
        binding.specialityEditText.error = if (isNotValid) "Invalid Speciality" else null
    }
    // MD ALERT
    private fun showmdExistAlert(isNotValid: Boolean) {
        binding.mdEditText.error = if (isNotValid) "Invalid MD Year" else null
    }
    // CONSULT ALERT
    private fun cnsltExistAlert(isNotValid: Boolean) {
        binding.cnsltPriceEditText.error = if (isNotValid) "Invalid Consultation Price" else null
    }
    // PROVINCE ALERT
    private fun provinceExistAlert(isNotValid: Boolean) {
        binding.provinceEditText.error = if (isNotValid) "Invalid Province" else null
    }
    // MUNICIPALITY ALERT
    private fun municipalityExistAlert(isNotValid: Boolean) {
        binding.municipalityEditText.error = if (isNotValid) "Invalid Municipality" else null
    }
    // BARANGGAY ALERT
    private fun brngyExistAlert(isNotValid: Boolean) {
        binding.brgyEditText.error = if (isNotValid) "Invalid Baranggay" else null
    }
    // HOUSE NUMBER ALERT
    private fun hnExistAlert(isNotValid: Boolean) {
        binding.hnEditText.error = if (isNotValid) "Invalid House Number" else null
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