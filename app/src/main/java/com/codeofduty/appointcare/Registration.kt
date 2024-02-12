package com.codeofduty.appointcare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast


class Registration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)



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

    }

    private fun showToast(gender: String) {
        Toast.makeText(this, "You chose $gender", Toast.LENGTH_SHORT).show()
    }

    private fun showToast(gender: String, registerAS: String) {
        Toast.makeText(this, "You chose $registerAS", Toast.LENGTH_SHORT).show()
    }

}
