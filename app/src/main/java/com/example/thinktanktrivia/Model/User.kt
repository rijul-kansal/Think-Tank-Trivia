package com.example.thinktanktrivia.Model

/**
 * Used this Model class to store user data into FireStore
 */

data class User(
    var name:String="",
    var id :String="",
    var mobileNo :String="",
    var image:String="",
    var email:String=""

)
