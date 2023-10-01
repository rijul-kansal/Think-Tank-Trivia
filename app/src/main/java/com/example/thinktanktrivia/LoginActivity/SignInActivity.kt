package com.example.thinktanktrivia.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.thinktanktrivia.Activity.BaseActivity
import com.example.thinktanktrivia.Activity.MainActivity
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.Utils.Constants
import com.example.thinktanktrivia.databinding.ActivitySignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignInActivity : BaseActivity() {
    lateinit var binding:ActivitySignInBinding
    // Firebase instance
    lateinit var mAuth:FirebaseAuth

    companion object
    {
        private val SIGN_IN_CODE=100
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()
        mAuth=FirebaseAuth.getInstance()
        binding.SignUpBtn.setOnClickListener {
            LoginNewUser()
        }

        binding.circularImageGoogle.setOnClickListener {
            signIn()
        }

        binding.circularImgPhone.setOnClickListener {
            val intent=Intent(this,MobileVerificationActivity::class.java)
            intent.putExtra(Constants.USER_SIGN_IN_MOBILE_VERIFICATION,1)
            startActivity(intent)
            finish()
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
    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        showProgressBar(this@SignInActivity," ")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.d("Main","Failed ${e.message}")
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                cancelProgressBar()
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast(this, "Authentication failed")
                }
            }
    }
}