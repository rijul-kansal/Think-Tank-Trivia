package com.example.thinktanktrivia.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.thinktanktrivia.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreen2 : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen2)

        // code for getting full window screen
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // FireBase instance
        mAuth= FirebaseAuth.getInstance()

        // check if user is there or not
        val user=mAuth.currentUser

        // Generate Delay of 1.5 second
        Handler().postDelayed({

            // if user is there and uid is algo there then login to main activity else login to intro activity
            if(user!=null) {
                 if(user.uid.isNotEmpty()) {
                     startActivity(Intent(this, MainActivity::class.java))
                 }else {
                     startActivity(Intent(this, IntroActivity::class.java))
                 }
            } else {
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        }, 1500)

    }
}