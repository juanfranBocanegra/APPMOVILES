package com.app.dolt

import android.app.Application
import android.util.Log
import com.app.dolt.api.RetrofitClient
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializa RetrofitClient con el contexto de la aplicación
        RetrofitClient.initialize(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree()) // Árbol de logs para desarrollo
        }
    }
}

