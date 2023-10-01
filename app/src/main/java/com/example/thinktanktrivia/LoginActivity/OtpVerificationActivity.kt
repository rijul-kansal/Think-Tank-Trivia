package com.example.thinktanktrivia.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.thinktanktrivia.Activity.BaseActivity
import com.example.thinktanktrivia.Activity.MainActivity
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.databinding.ActivityOtpVerificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import java.util.Timer
import java.util.TimerTask

class OtpVerificationActivity : BaseActivity() {
    lateinit var phoneNumber: String
    var timeoutSeconds = 60L
    lateinit var verificationCode: String
    var mAuth = FirebaseAuth.getInstance()


    lateinit var binding:ActivityOtpVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityOtpVerificationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()
        startResendTimer()
        if(intent.hasExtra("phoneNo"))
        {
            phoneNumber= intent.getStringExtra("phoneNo").toString()
            verificationCode=intent.getStringExtra("verificationCode").toString()
            Log.d("Main","Phone No ${phoneNumber}  ${verificationCode}")
        }

        binding.SignUpBtnOtp.setOnClickListener {
            val enteredOtp: String = binding.pinview.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationCode!!, enteredOtp)
            signIn(credential)
        }
    }
    private fun SetUpToolbar() {
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
    fun signIn(phoneAuthCredential: PhoneAuthCredential?) {
        //login and go to next activity
       showProgressBar(this@OtpVerificationActivity," ")
        mAuth.signInWithCredential(phoneAuthCredential!!).addOnCompleteListener { task ->
            cancelProgressBar()
            if (task.isSuccessful) {
                val intent = Intent(this@OtpVerificationActivity, MainActivity::class.java)
                intent.putExtra("phone", phoneNumber)
                startActivity(intent)
            } else {
                Toast(this@OtpVerificationActivity,"Opt Verification Failed")
            }
        }
    }

    fun startResendTimer() {
        binding.resend.setEnabled(false)
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeoutSeconds--
                binding.resend.setText("Resend OTP in $timeoutSeconds seconds")
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 60L
                    timer.cancel()
                    runOnUiThread {binding.resend.setEnabled(true) }
                }
            }
        }, 0, 1000)
    }
}