package com.codeofduty.appointcare.api

import User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("users")
    fun getUsersWithDoctorRole(): Call<List<User>>

    @POST("signup")
    fun registerUser(@Body user: User): Call<User>
}