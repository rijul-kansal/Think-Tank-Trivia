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
        // Firebase instance
        mAuth = FirebaseAuth.getInstance()

       // signing new user using email and password
        binding.SignUpBtn.setOnClickListener {
            registerNewUser()
        }
        // signing up using google ids
        binding.circularImageGoogle.setOnClickListener { signIn() }

        // signing up using mobile no
        binding.circularImgPhone.setOnClickListener {
            startActivity(Intent(this,MobileVerificationActivity::class.java))
        }
       // going to sign in  activity
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

        // Take the value of the  edittext in Strings
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
        // checking is pattern is matching or not
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        } else {
            Toast(this@SignUpActivity,"Please Enter valid Email address")
            return
        }

        //Registering User using email and password
        mAuth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // if successfully created a new user then store his name id and email into user model
                    val user= mAuth.currentUser?.let { User(name=name,id= it.uid,email=email) }
                    // if successfully created a new user then store his name id and email into Firestore
                    if (user != null) {
                        FireStoreClass().AddUserToFireBase(user)
                    }
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
        // getting current user
        val user = FirebaseAuth.getInstance().currentUser
        // sending verification link to new user who sign up using email and pass
        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // after sending email signout the present user and take him to the main activity
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    finish()
                } else {
                    //This line disables the transition animation when finishing the current activity.
                    // It sets both the enter and exit animations to zero, effectively making the transition instant.
                    overridePendingTransition(0, 0)
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(intent)
                }
            }
    }
    // Sign up using Google
    private fun signIn() {
        // create instance of google sign in options  with default sign in options
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