package com.codeofduty.appointcare.api

import com.codeofduty.appointcare.model.User
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    fun getUsersWithDoctorRole(): Call<List<User>>
}