package com.app.dolt

import android.app.Application
import com.app.dolt.api.RetrofitClient

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializa RetrofitClient con el contexto de la aplicación
        RetrofitClient.initialize(this)
    }
}