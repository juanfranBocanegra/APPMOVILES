package com.app.dolt.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
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

        finish()
    }

    private fun checkTokenValidity(token: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.validateToken()
                if (response.isSuccessful) {
                    // El token es válido, redirigir a FeedCActivity
                    navigateToFeedC()
                } else {
                    // El token no es válido, redirigir a LoginActivity
                    navigateToLogin()
                }
            } catch (e: Exception) {
                // Error de red o servidor, redirigir a LoginActivity
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToFeedC() {
        val intent = Intent(this, FeedCActivity::class.java)
        startActivity(intent)
        finish()
    }
}


