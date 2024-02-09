package com.codeofduty.appointcare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val btnLogIn: Button = findViewById(R.id.btn_SignInLogIn)

        btnLogIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the Main activity
                val intent = Intent(this@LogIn, MainActivity::class.java)
                startActivity(intent)
            }
        })
    }
}