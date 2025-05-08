package com.app.dolt

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.dolt.api.RetrofitClient
import com.app.dolt.data.AppDatabase
import timber.log.Timber
import com.google.firebase.messaging.FirebaseMessaging


/**
 * Clase de aplicación principal que se ejecuta al iniciar la aplicación.
 * Se encarga de inicializar los componentes necesarios a nivel de aplicación.
 */
class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
            private set

        val appContext: Context
            get() = instance.applicationContext
    }

    /**
     * Método llamado cuando la aplicación es creada.
     * Inicializa el cliente Retrofit y configura Timber para el registro de los logs en modo desarrollo.
     */
    override fun onCreate() {
        super.onCreate()
        instance = this
        // Inicializa RetrofitClient con el contexto de la aplicación
        RetrofitClient.initialize(this)

        // Suscribir al tema "todos"
        FirebaseMessaging.getInstance().subscribeToTopic("global")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Suscrito al tema 'global'")
                } else {
                    Log.e("FCM", "Error al suscribir", task.exception)
                }
            }

        // Obtener token (para debug)
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCM", "Token: ${task.result}")
            }
        }


        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree()) // Árbol de logs para desarrollo
        }
    }


}

