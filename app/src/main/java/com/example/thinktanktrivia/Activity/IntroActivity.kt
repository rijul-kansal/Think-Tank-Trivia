package com.example.thinktanktrivia.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.LoginActivity.SignInActivity
import com.example.thinktanktrivia.LoginActivity.SignUpActivity
import com.example.thinktanktrivia.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        SetUpToolbar()
        binding.SignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
        binding.SignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
    // code for setting own toolbar
    private fun SetUpToolbar()
    {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()?.setDisplayShowHomeEnabled(true)
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            supportActionBar!!.title=resources.getString(R.string.Intro_string)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}