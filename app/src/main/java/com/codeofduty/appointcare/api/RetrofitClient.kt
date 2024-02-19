package com.codeofduty.appointcare.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
interface RetrofitClient {
    companion object {
        private const val BASE_URL = "https://appointment-care-api.vercel.app/api/v1/auth/"

        fun getService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }

}