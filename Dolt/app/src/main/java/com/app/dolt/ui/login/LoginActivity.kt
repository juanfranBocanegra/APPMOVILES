package com.app.dolt.ui.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.app.dolt.R
import com.app.dolt.databinding.ActivityLoginBinding
import com.app.dolt.ui.challenge.FeedCActivity

class LoginActivity : AppCompatActivity() {

    // Referencia al binding y al ViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Obtén SharedPreferences
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

        // Crea el ViewModel usando el Factory
        val factory = LoginViewModelFactory(sharedPreferences)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        // Escuchar el clic del botón de login
        binding.btnLogin.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Llamar al método de login en el ViewModel
                loginViewModel.login(username, password,
                    onSuccess = { token ->
                        // Guardamos el token en SharedPreferences y mostramos un mensaje
                        Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                        // Llamamos a MainActivity
                        Log.i("TOKEN: ", token)

                        navigateToMainActivity()
                    },
                    onError = { errorMessage ->
                        // Mostrar mensaje de error si algo salió mal
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                // Si los campos están vacíos
                Toast.makeText(this, "Por favor, ingrese los datos correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Navegar a la actividad principal
    private fun navigateToMainActivity() {
        val intent = Intent(this, FeedCActivity::class.java)
        startActivity(intent)
        finish()  // Finalizar LoginActivity para que no se pueda volver atrás
    }
}
