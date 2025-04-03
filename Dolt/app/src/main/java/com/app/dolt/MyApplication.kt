package com.app.dolt

import android.app.Application
import android.util.Log
import com.app.dolt.api.RetrofitClient
import timber.log.Timber


/**
 * Clase de aplicación principal que se ejecuta al iniciar la aplicación.
 * Se encarga de inicializar los componentes necesarios a nivel de aplicación.
 */
class MyApplication : Application() {

    /**
     * Método llamado cuando la aplicación es creada.
     * Inicializa el cliente Retrofit y configura Timber para el registro de los logs en modo desarrollo.
     */
    override fun onCreate() {
        super.onCreate()
        // Inicializa RetrofitClient con el contexto de la aplicación
        RetrofitClient.initialize(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree()) // Árbol de logs para desarrollo
        }
    }
}

