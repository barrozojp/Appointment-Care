package com.codeofduty.appointcare.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.codeofduty.appointcare.R

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegPatient: Button = findViewById(R.id.btn_RegPatient)

        btnRegPatient.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the PatientRegister activity
                val intent = Intent(this@Register, PatientRegister::class.java)
                startActivity(intent)
            }
        })
        val btnRegDoctor: Button = findViewById(R.id.btn_RegDoctor)

        btnRegDoctor.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the DoctorRegister activity
                val intent = Intent(this@Register, DoctorRegister::class.java)
                startActivity(intent)
            }
        })
    }
}
