package com.codeofduty.appointcare.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.codeofduty.appointcare.R

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME: Long = 3500


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            startActivity(Intent (this, SignInRegister::class.java))
            finish()

        }, SPLASH_TIME)
    }
}