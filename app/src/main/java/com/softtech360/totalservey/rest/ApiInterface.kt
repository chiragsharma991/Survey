package com.softtech360.totalservey.rest

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @POST("create-survey")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun createSurvey(@Body jsonObject: JsonObject): Call<JsonObject>

    @POST("get-question-answer")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun getQuestionAnswer(): Call<JsonObject>

    @POST("login")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun login(@Body jsonObject: JsonObject): Call<JsonObject>

    @POST("reset-password")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun reset_password(@Body jsonObject: JsonObject): Call<JsonObject>


}