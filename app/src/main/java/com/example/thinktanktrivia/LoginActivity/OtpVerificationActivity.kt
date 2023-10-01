package com.example.thinktanktrivia.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.thinktanktrivia.Activity.BaseActivity
import com.example.thinktanktrivia.Activity.MainActivity
import com.example.thinktanktrivia.FireBase.FireStoreClass
import com.example.thinktanktrivia.Model.User
import com.example.thinktanktrivia.R
import com.example.thinktanktrivia.Utils.Constants
import com.example.thinktanktrivia.databinding.ActivityOtpVerificationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import java.util.Timer
import java.util.TimerTask

class OtpVerificationActivity : BaseActivity() {
    // used to carry current mobile no
    lateinit var phoneNumber: String
    // TODO we will implement later
    var timeoutSeconds = 60L
    lateinit var verificationCode: String
    var mAuth = FirebaseAuth.getInstance()

    var identificationNo=0
    lateinit var binding:ActivityOtpVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityOtpVerificationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        SetUpToolbar()
//        startResendTimer()
        if(intent.hasExtra("phoneNo"))
        {
            phoneNumber= intent.getStringExtra("phoneNo").toString()
            verificationCode=intent.getStringExtra("verificationCode").toString()
            Log.d("Main","Phone No ${phoneNumber}  ${verificationCode}")
            identificationNo=intent.getIntExtra(Constants.USER_SIGN_IN_MOBILE_VERIFICATION_OTP,0)
            if(identificationNo==12)
            {
                binding.toolbar.title=resources.getString(R.string.Sign_in)
                binding.SignUpBtnOtp.text=resources.getString(R.string.Sign_in)
            }
        }

        binding.SignUpBtnOtp.setOnClickListener {
            val enteredOtp: String = binding.pinview.text.toString()
            val credential = PhoneAuthProvider.getCredential(verificationCode!!, enteredOtp)
            signIn(credential)
        }
        binding.resend.visibility= View.GONE
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
    // this will match otp and credential and if matches then put us on main activity
    fun signIn(phoneAuthCredential: PhoneAuthCredential?) {
        //login and go to next activity
       showProgressBar(this@OtpVerificationActivity," ")
        mAuth.signInWithCredential(phoneAuthCredential!!).addOnCompleteListener { task ->
            cancelProgressBar()
            if (task.isSuccessful) {
                Log.d("Main" ,"IdentificationNo2 ${identificationNo}")
                if(identificationNo==0) {
                    val user = User(id = mAuth.currentUser!!.uid, mobileNo = phoneNumber)
                    FireStoreClass().AddUserToFireBase(user)
                }
                val intent = Intent(this@OtpVerificationActivity, MainActivity::class.java)
                intent.putExtra("phone", phoneNumber)
                startActivity(intent)
            } else {
                Toast(this@OtpVerificationActivity,"Opt Verification Failed")
            }
        }
    }
      // TODO implement later
//    fun startResendTimer() {
//        binding.resend.setEnabled(false)
//        val timer = Timer()
//        timer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                timeoutSeconds--
//                binding.resend.setText("Resend OTP in $timeoutSeconds seconds")
//                if (timeoutSeconds <= 0) {
//                    timeoutSeconds = 60L
//                    timer.cancel()
//                    runOnUiThread {binding.resend.setEnabled(true) }
//                }
//            }
//        }, 0, 1000)
//    }
}