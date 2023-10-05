package com.example.thinktanktrivia.Model

import java.io.Serializable

data class Res(
    var category:String="",
    var Difficulty:String="",
    var corrQ: Int =0,
    var totalQ: Int =0,
    var type:String="",
):Serializable
