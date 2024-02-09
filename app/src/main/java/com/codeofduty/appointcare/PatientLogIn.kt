package com.codeofduty.appointcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class PatientLogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_log_in)

        val btnLogIn: Button = findViewById(R.id.btn_PatientLogIn)

        btnLogIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the Main activity for Patient
                val intent = Intent(this@PatientLogIn, MainActivityPatient::class.java)
                startActivity(intent)
            }
        })
    }
}