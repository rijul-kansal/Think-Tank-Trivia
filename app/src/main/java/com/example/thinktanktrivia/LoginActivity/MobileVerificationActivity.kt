package com.example.thinktanktrivia.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.thinktanktrivia.Utils.BaseActivity
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.Utils.Constants
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
    // used to get current phone No
    lateinit var phoneNumber: String
    // after how much time opt will be invalid
    var timeoutSeconds = 60L
    // verification code for otp
    var verificationCode: String? = null
    // helps in resending otp
    var resendingToken: ForceResendingToken? = null
    var mAuth = FirebaseAuth.getInstance()
    lateinit var binding:ActivityMobileVerificationBinding
    // used to check from which activity it is comming like from sign up or sign in
    var identification_no=0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMobileVerificationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()


        binding.SignUpBtn.setOnClickListener {
            sendOtp()
        }
         // if comming from sign in activity then no need of name and change toolbar title as sign in and same for btn
        identification_no=intent.getIntExtra(Constants.USER_SIGN_IN_MOBILE_VERIFICATION,0)
        if(identification_no==1)
        {
            binding.etName.visibility= View.GONE
            binding.toolbar.title=resources.getString(R.string.Sign_in)
            binding.SignUpBtn.text=resources.getString(R.string.Sign_in)
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
    // this fn will send the otp and put us on next activity for the first time
    fun sendOtp() {
        // checking if mobile no and name are empty or not
        phoneNumber =binding.etPhoneNo.text.toString()
        if(phoneNumber!!.isEmpty())
        {
            Toast(this@MobileVerificationActivity,"Please Enter Your Mobile No")
            return
        }
        if(binding.etName.text!!.isEmpty() && identification_no==0)
        {
            Toast(this@MobileVerificationActivity,"Please Enter Your Name")
            return
        }
        // if not then proceed futher
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
                    intent.putExtra(Constants.PHONE_NO,phoneNumber)
                    intent.putExtra(Constants.VERIFICATION_CODE,verificationCode)
                    intent.putExtra(Constants.NAME_INTENT,binding.etName.text.toString())
                    Log.d("Main" ,"IdentificationNo1 ${identification_no}")
                    if(identification_no==1)
                    intent.putExtra(Constants.USER_SIGN_IN_MOBILE_VERIFICATION_OTP,2)
                    startActivity(intent)
                    finish()
                    cancelProgressBar()
                }
            })
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
    }

}