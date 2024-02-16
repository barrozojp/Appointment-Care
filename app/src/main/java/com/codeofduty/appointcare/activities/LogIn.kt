package com.codeofduty.appointcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.codeofduty.appointcare.R

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val btnLogInDoctor: Button = findViewById(R.id.btn_LogInDoctor)
        val btnLogInPatient: Button = findViewById(R.id.btn_LogInPatient)

        btnLogInDoctor.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the Doctor LogIN
                val intent = Intent(this@LogIn, DoctorLogIn::class.java)
                startActivity(intent)
            }
        })
        btnLogInPatient.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the Patient LogIN
                val intent = Intent(this@LogIn, PatientLogIn::class.java)
                startActivity(intent)
            }
        })
    }

}