package com.example.thinktanktrivia.Api

import com.example.thinktanktrivia.Model.QuestionModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/api.php")
    suspend fun getSeries(
        @Query("amount") amount:Int,
        @Query("category") category:Int,
        @Query("difficulty") difficulty:String,
        @Query("type") type:String,
    ): Response<QuestionModel>
}