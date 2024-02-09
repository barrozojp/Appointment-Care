package com.codeofduty.appointcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class DoctorLogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_log_in)

        val btnLogIn: Button = findViewById(R.id.btn_DoctorLogIn)

        btnLogIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the Main activity
                val intent = Intent(this@DoctorLogIn, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
}