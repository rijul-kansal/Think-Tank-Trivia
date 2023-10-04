package com.example.thinktanktrivia.Model

import java.io.Serializable

data class QuestionModel(
    val response_code: Int,
    val results: List<Result>
):Serializable