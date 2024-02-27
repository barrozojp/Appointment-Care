package com.codeofduty.appointcare.api

import DoctorUsers
import User
import com.codeofduty.appointcare.models.LoginResponse
import com.codeofduty.appointcare.models.UserLoginResponse
import com.codeofduty.appointcare.models.LoginUser
import patientRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //SEARCH METHOD
    @GET("person/users")
    fun getUsersWithDoctorRole(): Call<List<DoctorUsers>>

    // SIGN UP DOCTOR
    @POST("auth/Signup")
    fun registerDoctor(@Body user: User): Call<User>

    // SIGN UP PATIENT
    @POST("auth/Signup")
    fun registerPatient(@Body patientForm: patientRegister): Call<patientRegister>


    //SIGN IN PATIENT
    //@POST("auth/Login")
    //fun signinUser(@Body user: LoginUser): Call<LoginResponse>

    //SIGN IN DOCTOR
    @POST("auth/Login")
    fun signinUser(@Body user: LoginUser): Call<UserLoginResponse>
}