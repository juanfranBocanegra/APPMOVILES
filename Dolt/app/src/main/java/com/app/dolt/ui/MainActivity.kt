package com.app.dolt.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.dolt.R
import androidx.lifecycle.lifecycleScope
import com.app.dolt.api.RetrofitClient
import com.app.dolt.ui.challenge.FeedCActivity
import com.app.dolt.ui.login.LoginActivity
import kotlinx.coroutines.launch


/**
 * Actividad principal que se ejecuta al iniciar la aplicación.
 * Comprueba la validez del token de autenticación y redirige al usuario a la pantalla correspondiente.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Método llamado al crear la actividad.
     * Comprueba si existe un token válido y redirige al usuario a la pantalla adecuada.
     *
     * @param savedInstanceState _ Estado anterior guardado (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val token = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE).getString("ACCESS_TOKEN", null)
        val username = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE).getString("USERNAME", null)

        if (token == null || username == null) {
            navigateToLogin()
        }else{
            checkTokenValidity(token)
        }
    }

    /**
     * Comprueba si el token almacenado es válido consultando la API.
     * Si es válido, redirige al feed de desafíos. Si no, redirige a la pantalla de login.
     *
     * @param token : Token de autenticación.
     */
    private fun checkTokenValidity(token: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.validateToken()

                if (response.isSuccessful) {
                    navigateToFeedC()
                } else {
                    navigateToLogin()
                }
            } catch (e: Exception) {
                navigateToLogin()
            }
        }
    }


    /**
     * Redirige al usuario a la pantalla de inicio de sesión.
     */
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Redirige al usuario al feed de desafíos.
     */
    private fun navigateToFeedC() {
        val intent = Intent(this, FeedCActivity::class.java)
        startActivity(intent)
        finish()
    }
}


