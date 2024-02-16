package com.codeofduty.appointcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.codeofduty.appointcare.R

class SignInRegister : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_register)

        val btnSignIn: Button = findViewById(R.id.btn_SignIn)

        btnSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the LogIn activity
                val intent = Intent(this@SignInRegister, LogIn::class.java)
                startActivity(intent)
            }
        })

        val btnRegister: Button = findViewById(R.id.btn_Register)

        btnRegister.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                // Launch the Register activity
                val intent = Intent(this@SignInRegister, Registration::class.java)
                startActivity(intent)
            }
        })
    }
}
