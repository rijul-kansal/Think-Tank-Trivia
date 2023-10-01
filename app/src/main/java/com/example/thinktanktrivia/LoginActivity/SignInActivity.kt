package com.example.thinktanktrivia.LoginActivity

import android.content.Intent
import android.os.Bundle
import com.example.thinktanktrivia.Activity.BaseActivity
import com.example.thinktanktrivia.Activity.MainActivity
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : BaseActivity() {
    lateinit var binding:ActivitySignInBinding
    // Firebase instance
    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()
        mAuth=FirebaseAuth.getInstance()
        binding.SignUpBtn.setOnClickListener {
            LoginNewUser()
        }
    }
    private fun SetUpToolbar()
    {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()?.setDisplayShowHomeEnabled(true)
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            supportActionBar!!.title=resources.getString(R.string.Sign_in)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    // login user code
    private fun LoginNewUser()
    {
        // getting crenditial  from edit text
        val email=binding.etEmail.text.toString()
        val password=binding.etPassword.text.toString()
        // checking validation
        if(email.isEmpty())
        {
            Toast(this@SignInActivity,"Please Enter Your Email")
            return
        }
        if(password.isEmpty())
        {
            Toast(this@SignInActivity,"Please Enter Your Password Correctly")
            return
        }
        // singing in
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (!task.isSuccessful) {
                    Toast(this@SignInActivity,"${task.exception?.message.toString()}")
                } else {
                    Toast(this@SignInActivity,"Please verify your email")
                    checkIfEmailVerified()
                }
            }

    }
    // only login if user is verified
    private fun checkIfEmailVerified() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user!!.isEmailVerified) {
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()

        } else {
            FirebaseAuth.getInstance().signOut()
        }
    }
}