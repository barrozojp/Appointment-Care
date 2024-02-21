package com.codeofduty.appointcare.api

import DoctorUsers
import User
import com.codeofduty.appointcare.models.LoginUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //SEARCH METHOD
    @GET("person/users")
    fun getUsersWithDoctorRole(): Call<List<DoctorUsers>>

    // SIGN UP
    @POST("auth/Signup")
    fun registerUser(@Body user: User): Call<User>

    //SIGN IN
    @POST("auth/Login")
    fun signinUser(@Body user: LoginUser): Call<User>
}