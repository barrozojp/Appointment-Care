package com.codeofduty.appointcare.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.codeofduty.appointcare.R

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME: Long = 3500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // Check if the user is already logged in
            val (isLoggedIn, role) = isLoggedIn()
            val targetActivity = when {
                isLoggedIn -> {
                    if (role == "Doctor") MainActivity::class.java
                    else MainActivityPatient::class.java
                }
                else -> SignInRegister::class.java
            }
            startActivity(Intent(this, targetActivity))
            finish()
        }, SPLASH_TIME)
    }

    private fun isLoggedIn(): Pair<Boolean, String?> {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val role = sharedPreferences.getString("role", null)
        Log.d("Role", "Role: $role") // Log the role for debugging purposes
        return Pair(!token.isNullOrEmpty(), role)
    }
}
