import android.content.Context
import android.content.SharedPreferences
import com.codeofduty.appointcare.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroClient {
    private const val BASE_URL = "https://appointment-care-api.vercel.app/api/v1/auth/"
    private const val PREF_NAME = "MyAppPrefs"
    private const val TOKEN_KEY = "token"

    // Initialize Retrofit with OkHttpClient
    fun getService(context: Context): ApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                // Retrieve token from SharedPreferences
                val token = getToken(context)
                // Add token to the request headers
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }

    // Save the token to SharedPreferences
    fun saveToken(context: Context, token: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    // Retrieve the token from SharedPreferences
    private fun getToken(context: Context): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(TOKEN_KEY, null)
    }
}
