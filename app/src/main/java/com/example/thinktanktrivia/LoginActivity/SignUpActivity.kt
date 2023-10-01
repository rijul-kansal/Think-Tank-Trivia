package com.example.thinktanktrivia.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.example.thinktanktrivia.Activity.BaseActivity
import com.example.thinktanktrivia.Activity.MainActivity
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


class SignUpActivity : BaseActivity() {
    lateinit var binding:ActivitySignUpBinding
    // FireBase instance
    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()
        mAuth = FirebaseAuth.getInstance()


        binding.SignUpBtn.setOnClickListener {
            registerNewUser()
        }

    }

    // code for setting up toolbar
    private fun SetUpToolbar()
    {
        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
            getSupportActionBar()?.setDisplayShowHomeEnabled(true)
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
            supportActionBar!!.title=resources.getString(R.string.Sign_up)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    // registering new users code
    private fun registerNewUser() {

        // show the visibility of progress bar to show loading
        showProgressBar(this@SignUpActivity,"")

        // Take the value of two edit texts in Strings
        val email: String
        val password: String
        val name:String
        email = binding.etEmail.getText().toString().trim()
        password = binding.etPassword.getText().toString().trim()
        name = binding.etName.getText().toString()
        // checking validations
        if (TextUtils.isEmpty(email)) {
            Toast(this@SignUpActivity,"Please enter email!!")
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast(this@SignUpActivity,"Please enter password!!")
            return
        }
        if (TextUtils.isEmpty(name)) {
            Toast(this@SignUpActivity,"Please enter name!!")
            return
        }
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        } else {
            Toast(this@SignUpActivity,"Please Enter valid Email address")
            return
        }
        //Registering User
        mAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // is Successfully Reg then send email verification link
                    sendVerificationEmail()
                    Toast(this@SignUpActivity,"Registered Successfully")
                    // hide the progress bar
                    cancelProgressBar()
                } else {
                    // Registration failed
                    Toast(this@SignUpActivity,"${task.exception!!.message.toString()}")
                    // hide the progress bar
                    cancelProgressBar()
                }
            }
    }
    // Sending verification link to the new user
    private fun sendVerificationEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    finish()
                } else {
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                }
            }
    }
}