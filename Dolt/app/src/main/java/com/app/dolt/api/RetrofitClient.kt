package com.app.dolt.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.app.dolt.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.io.IOException


/**
 * Objeto singleton que gestiona la configuración y creación del cliente Retrofit para la aplicación.
 * Incluye configuración de URL base, interceptores,  autorización mediante token y caché HTTP.
 */
object RetrofitClient {

    //Configuración de la URL base y del host
    //private const val URL = "http://192.168.1.100:8000/api/"
    private const val HTTP = "http://"
    private var HOST = "85.50.17.80"
    private const val HOST_EMU = "10.0.2.2"
    private const val PORT = ":80"
    private const val ENDPOINT = "/api/"
    private lateinit var URL : String
    lateinit var DOMAIN : String

    // Configuración de la caché
    private const val CACHE_SIZE = 10 * 1024 * 1024 // 10 MB
    private const val MAX_AGE = 60 // 1 minuto (en producción usa valores mayores)

    private lateinit var sharedPreferences: SharedPreferences

    /**
     * Inicializador del objeto. Define la URL base y el dominio de la API.
     */
    init {

        /*URL = if (isHostReachable(HOST)){
            HTTP + HOST + PORT + ENDPOINT
        } else{
            HTTP + HOST_EMU + PORT + ENDPOINT
        }*/
        DOMAIN = HTTP + HOST + PORT
        URL = DOMAIN + ENDPOINT

        Timber.i(URL)
    }

    /**
     * Crea y configura la caché HTTP para las peticiones.
     * 
     * @param context : Contexto de la aplicación.
     * @return Instancia de [Cache].
     */
    private fun createCache(context: Context): Cache {
        return Cache(File(context.cacheDir, "http_cache"), CACHE_SIZE.toLong())
    }

    /**
     * Inicializa el cliente Retrofit y las preferencias compartidas.
     * 
     * @param context : Contexto de la aplicación.
     */
    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
    }

    /**
     * Cliente HTTP configurado con interceptores para logging y autorización.
     */
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

    /**
     * Comprueba si un host es alcanzable mediante un ping.
     * 
     * @param host : Dirección del host a comprobar.
     * @return 'true' si el host responde al ping, 'false' en caso contrario.
     */
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

    /**
     * Instancia de Retrofit configurada con la URL base y el cliente HTTP
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(URL) // Cambia la URL de la API
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Servicio de la API que expone los endpoints definidos en [AppService].
     */
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
