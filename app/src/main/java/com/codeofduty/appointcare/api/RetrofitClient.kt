package com.codeofduty.appointcare.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response


interface RetrofitClient {
    companion object {
        private const val BASE_URL = "https://appointment-care-api.vercel.app/api/v1/"

        fun getService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }

        private const val PREF_NAME = "MyAppPrefs"
        private const val TOKEN_KEY = "token"

        fun createService(context: Context): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(AuthInterceptor(context))
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }

        fun saveToken(context: Context, token: String) {
            val prefs: SharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            prefs.edit().putString(TOKEN_KEY, token).apply()
        }

        private fun getToken(context: Context): String? {
            val prefs: SharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return prefs.getString(TOKEN_KEY, null)
        }

        private class AuthInterceptor(val context: Context) : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val token = getToken(context)
                val role = getRoleFromSharedPreferences(context)
                val request = chain.request().newBuilder()

                // Add token to the request headers if available
                token?.let {
                    request.addHeader("Authorization", "Bearer $token")
                }
                // Add role to the request headers if available
                role?.let {
                    request.addHeader("Role", role)
                }


                return chain.proceed(request.build())
            }
        }
        // Function to retrieve role from SharedPreferences
        private fun getRoleFromSharedPreferences(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            return sharedPreferences.getString("role", null)
        }
        fun logout(context: Context) {
            val prefs: SharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

        }
    }
}
