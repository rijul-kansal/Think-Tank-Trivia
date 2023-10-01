package com.example.thinktanktrivia.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.thinktanktrivia.R

class SplashScreen1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen1)

        // Code For Getting Full window Size
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Code For Delay on this activity for 1.5 second
        Handler().postDelayed({
            startActivity(Intent(this, SplashScreen2::class.java))
            finish()
        }, 1500)
    }
}