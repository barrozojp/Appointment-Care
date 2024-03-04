package com.codeofduty.appointcare.api

import DoctorUsers
import User
import com.codeofduty.appointcare.models.BookAppointment
import com.codeofduty.appointcare.models.UserLoginResponse
import com.codeofduty.appointcare.models.LoginUser
import com.codeofduty.appointcare.models.MyBookings
import com.codeofduty.appointcare.models.Schedule
import com.codeofduty.appointcare.models.UserX
import patientRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    //SEARCH METHOD
    @GET("person/users")
    fun getUsersWithDoctorRole(): Call<List<DoctorUsers>>

    @GET("person/users/{userId}")
    fun appointDoctorDetails(@Path("userId") userId: String): Call<DoctorUsers>


    // SIGN UP DOCTOR
    @POST("auth/Signup")
    fun registerDoctor(@Body user: User): Call<User>

    // SIGN UP PATIENT
    @POST("auth/Signup")
    fun registerPatient(@Body patientForm: patientRegister): Call<patientRegister>

    //SIGN IN USER
    @POST("auth/Login")
    fun signinUser(@Body user: LoginUser): Call<UserLoginResponse>

    // EDIT DOCTOR PROFILE
    @PUT("person/users/{userId}")
    fun updateDoctorProfile(@Path("userId") userId: String, @Body user: UserX): Call<UserX>

    // EDIT Patient PROFILE
    @PUT("person/users/{userId}")
    fun updatePatientProfile(@Path("userId") userId: String, @Body user: UserX): Call<UserX>

    //BOOKING APPOINTMENT
    @POST("appoint/request/{userId}")
    fun bookAppointment(@Path("userId") userId: String, @Body book: BookAppointment): Call<BookAppointment>

    //GET MY BOOKINGS
    @GET("appoint/schedule/{userId}")
    fun getBookings(@Path("userId") userId: String): Call<MyBookings>

    //GET PENDING BOOKINGS
    @GET("appoint/schedule/{userId}")
    fun getPendingBookings(@Path("userId") userId: String): Call<MyBookings>

    //GET REJECTED BOOKINGS
    @GET("appoint/schedule/{userId}")
    fun getRejectedBookings(@Path("userId") userId: String): Call<MyBookings>


    //GET DOCTOR DETAILS
    @GET("person/users/{userId}")
    fun getDoctorDetails(@Path("userId") userId: String): Call<DoctorUsers>

}
