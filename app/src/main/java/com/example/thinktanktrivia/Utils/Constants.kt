package com.example.thinktanktrivia.Utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Constants {
    // Some Constants
    val USERS="users"
    val USER_SIGN_IN_MOBILE_VERIFICATION="useer_sign_in_mobile_verification"
    val USER_SIGN_IN_MOBILE_VERIFICATION_OTP="useer_sign_in_mobile_verification_otp"
    val PHONE_NO="phoneNo"
    val VERIFICATION_CODE="verificationCode"
    val GALLERY=3
    val NAME_INTENT="name"
    val UPDATE_PROFILE_CODE=4
    val EMAIL="email"
    val NAME="name"
    val IMAGE="image"
    val MOBILE_NO="mobileNo"
    val TOTAL_SCORE="totalscore"
    val CAT="cat"
    val AMT="amt"
    val Type="type"
    val Diff="diff"

    const val BASE_URL= "https://opentdb.com"
    fun getInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}