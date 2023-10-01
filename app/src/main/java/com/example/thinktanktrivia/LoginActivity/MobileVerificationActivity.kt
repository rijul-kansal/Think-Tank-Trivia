package com.example.thinktanktrivia.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.thinktanktrivia.Activity.BaseActivity
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.databinding.ActivityMobileVerificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.concurrent.TimeUnit


class MobileVerificationActivity : BaseActivity() {
    lateinit var phoneNumber: String
    var timeoutSeconds = 60L
    var verificationCode: String? = null
    var resendingToken: ForceResendingToken? = null
    var mAuth = FirebaseAuth.getInstance()
    lateinit var binding:ActivityMobileVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMobileVerificationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()


        binding.SignUpBtn.setOnClickListener {
            sendOtp()
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
    fun sendOtp() {
        phoneNumber =binding.etPhoneNo.text.toString()
        if(phoneNumber!!.isEmpty())
        {
            Toast(this@MobileVerificationActivity,"Please Enter Your Mobile No")
            return
        }
        showProgressBar(this@MobileVerificationActivity," ")
        val builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    cancelProgressBar()
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast(this@MobileVerificationActivity,"Otp Verification Failed")
                    Log.d("Main",e.message!!.toString())
                    cancelProgressBar()
                }

                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    super.onCodeSent(s, forceResendingToken)
                    verificationCode = s
                    resendingToken = forceResendingToken
                    Toast(this@MobileVerificationActivity,"Opt Send Successfully")
                    var intent=Intent(this@MobileVerificationActivity,OtpVerificationActivity::class.java)
                    intent.putExtra("phoneNo",phoneNumber)
                    intent.putExtra("verificationCode",verificationCode)
                    startActivity(intent)
                    finish()
                    cancelProgressBar()
                }
            })
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
    }

}