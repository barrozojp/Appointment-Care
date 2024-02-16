package com.codeofduty.appointcare.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.databinding.ActivityDoctorLogInBinding
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding2.widget.RxTextView


@SuppressLint("CheckResult")
class DoctorLogIn : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorLogInBinding.inflate(layoutInflater)
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


        val btnLogIn: Button = findViewById(R.id.btn_DoctorLogIn)

        btnLogIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the Main activity for Patient
                val intent = Intent(this@DoctorLogIn, MainActivity::class.java)
                startActivity(intent)
            }
        })
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
}