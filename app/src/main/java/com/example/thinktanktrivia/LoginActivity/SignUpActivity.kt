package com.example.thinktanktrivia.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.example.thinktanktrivia.Utils.BaseActivity
import com.example.thinktanktrivia.Activity.MainActivity
import com.example.thinktanktrivia.FireBase.FireStoreClass
import com.example.thinktanktrivia.Model.User
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignUpActivity : BaseActivity() {
    lateinit var binding:ActivitySignUpBinding
    // FireBase instance
    lateinit var mAuth:FirebaseAuth
    companion object {
        const val RC_SIGN_IN = 9001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()
        mAuth = FirebaseAuth.getInstance()


        binding.SignUpBtn.setOnClickListener {
            registerNewUser()
        }
        binding.circularImageGoogle.setOnClickListener { signIn() }

        binding.circularImgPhone.setOnClickListener {
            startActivity(Intent(this,MobileVerificationActivity::class.java))
        }

        binding.LoginBtn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
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
                    val user= mAuth.currentUser?.let { User(name=name,id= it.uid,email=email) }
                    // is Successfully Reg then send email verification link
                    if (user != null) {
                        FireStoreClass().AddUserToFireBase(user)
                    }
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
    // Sign up using Google
    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        showProgressBar(this@SignUpActivity," ")
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
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
                    val user= mAuth.currentUser?.let { mAuth.currentUser!!.displayName?.let { it1 -> mAuth.currentUser!!.email?.let { it2 -> User(name= it1,id= it.uid,email= it2) } } }
                    // is Successfully Reg then send email verification link
                    if (user != null) {
                        FireStoreClass().AddUserToFireBase(user)
                    }
//                    Toast(this, "Signed in as ${user?.displayName}")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast(this, "Authentication failed")
                }
            }
    }
}