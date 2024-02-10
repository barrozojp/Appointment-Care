package com.codeofduty.appointcare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast


class Registration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val roleSpinner:Spinner = findViewById(R.id.ROLESpinner)
        val ROLEOptions = arrayOf("Doctor", "Patient")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ROLEOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter


        val radioGroup = findViewById<RadioGroup>(R.id.gender)
        val maleRadioButton = findViewById<RadioButton>(R.id.gendermale)
        val femaleRadioButton = findViewById<RadioButton>(R.id.genderfemale)

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
    }

    private fun showToast(gender: String) {
        Toast.makeText(this, "You chose $gender", Toast.LENGTH_SHORT).show()
    }
}
