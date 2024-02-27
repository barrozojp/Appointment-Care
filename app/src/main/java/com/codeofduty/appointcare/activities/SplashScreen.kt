package com.codeofduty.appointcare.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.codeofduty.appointcare.R
import com.codeofduty.appointcare.models.UserX

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME: Long = 3500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // Check if the user is already logged in
            val userData = getUserData()
            val targetActivity = when {
                userData != null -> {
                    if (userData.role == "Doctor") MainActivity::class.java
                    else MainActivityPatient::class.java
                }
                else -> SignInRegister::class.java
            }
            startActivity(Intent(this, targetActivity))
            finish()
        }, SPLASH_TIME)
    }

    private fun getUserData(): UserX? {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val role = sharedPreferences.getString("role", null)
        return if (!role.isNullOrEmpty()) {
            UserX(role = role)
        } else {
            null
        }
    }
}
