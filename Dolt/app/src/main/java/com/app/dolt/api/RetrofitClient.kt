package com.app.dolt.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.app.dolt.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException



object RetrofitClient {
    //private const val URL = "http://192.168.1.100:8000/api/"
    private const val HTTP = "http://"
    private var HOST = BuildConfig.HOST
    private var HOST_EMU = "10.0.2.2"
    private const val PORT = ":8000"
    private const val ENDPOINT = "/api/"
    private lateinit var URL : String

    private lateinit var sharedPreferences: SharedPreferences

    init {

        URL = if (isHostReachable(HOST)){
            HTTP + HOST + PORT + ENDPOINT
        } else{
            HTTP + HOST_EMU + PORT + ENDPOINT
        }

    }

    fun initialize(context: Context) {

        sharedPreferences = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .addInterceptor { chain ->

            val token = sharedPreferences.getString("ACCESS_TOKEN", null)

            val request: Request = if (token != null) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }

            chain.proceed(request)
        }
        .build()


    private fun isHostReachable(host: String): Boolean {
        return try {
            val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 $host")
            val result = process.waitFor()
            result == 0
        } catch (e: IOException) {
            false
        } catch (e: InterruptedException) {
            false
        }
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL) // Cambia la URL de la API
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()



    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
