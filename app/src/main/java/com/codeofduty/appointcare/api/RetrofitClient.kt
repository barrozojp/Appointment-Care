package com.codeofduty.appointcare.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


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
                val token = TokenManger.getToken() ?: ""
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                return chain.proceed(request)
            }
        }
    }
}
