package com.codeofduty.appointcare.api

import User
import com.codeofduty.appointcare.models.LoginUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //SEARCH METHOD
    @GET("users")
    fun getUsersWithDoctorRole(): Call<List<User>>


    // SIGN UP
    @POST("signup")
    fun registerUser(@Body user: User): Call<User>

    //SIGN IN
    @POST("login")
    fun signinUser(@Body user: LoginUser): Call<User>
}