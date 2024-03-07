package com.codeofduty.appointcare.api

import DoctorUsers
import User
import com.codeofduty.appointcare.models.BookAppointment
import com.codeofduty.appointcare.models.ChangePassword
import com.codeofduty.appointcare.models.UserLoginResponse
import com.codeofduty.appointcare.models.LoginUser
import com.codeofduty.appointcare.models.MyBookings
import com.codeofduty.appointcare.models.UpdateBookingStatusRequest
import com.codeofduty.appointcare.models.UpdateSympConsPres
import com.codeofduty.appointcare.models.UserX
import okhttp3.MultipartBody
import okhttp3.RequestBody
import patientRegister
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    //SEARCH METHOD
    @GET("person/users")
    fun getUsersWithDoctorRole(): Call<List<DoctorUsers>>

    @GET("person/users/{userId}")
    fun appointDoctorDetails(@Path("userId") userId: String): Call<DoctorUsers>


    // SIGN UP DOCTOR
    @Multipart
    @POST("auth/Signup")
    fun registerDoctor(
        @Part("role") role: RequestBody,
        @Part("Fname") fName: RequestBody,
        @Part("Lname") lName: RequestBody,
        @Part("number") phoneNumber: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("email") email: RequestBody,
        @Part("age") age: RequestBody,
        @Part("specialty") specialty: RequestBody,
        @Part("md") md: RequestBody,
        @Part("consultPrice") consultPrice: RequestBody?,
        @Part("province") province: RequestBody?,
        @Part("municipality") municipality: RequestBody?,
        @Part("barangay") barangay: RequestBody?,
        @Part("hn") hn: RequestBody?,
        @Part("password") password: RequestBody,
        @Part("f2f") f2f: RequestBody?,
        @Part("online") online: RequestBody?,
        @Part("status") status: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<User>

    // SIGN UP PATIENT
    @Multipart
    @POST("auth/Signup")
    fun registerPatient(
        @Part("role") role: RequestBody,
        @Part("Fname") fName: RequestBody,
        @Part("Lname") lName: RequestBody,
        @Part("number") phoneNumber: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("age") age: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part image: MultipartBody.Part? // Image part
    ): Call<patientRegister>


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

    //GET ACCEPTED BOOKINGS
    @GET("appoint/schedule/{userId}")
    fun getAcceptedBookings(@Path("userId") userId: String): Call<MyBookings>

    //ACCEPT, REJECT, PENDING
    @PUT("appoint/verify/{userId}")
    fun updateBookingStatus(@Path("userId") userId: String, @Body requestBody: UpdateBookingStatusRequest): Call<MyBookings>

    //ADD SYMPTOMS, CONSULTATION AND PRESCRIPTION
    @PUT("appoint/consult/{userId}")
    fun addSymptomsObsAndPres(@Path("userId") userId: String, @Body requestBody: UpdateSympConsPres): Call<MyBookings>

    //GET DOCTOR DETAILS
    @GET("person/users/{userId}")
    fun getDoctorDetails(@Path("userId") userId: String): Call<DoctorUsers>

    //CHANGE PASSWORD
    @PUT("person/password/{userId}")
    fun changePassword(@Path("userId") userId: String, @Body changePassword: ChangePassword): Call<Any>
}
