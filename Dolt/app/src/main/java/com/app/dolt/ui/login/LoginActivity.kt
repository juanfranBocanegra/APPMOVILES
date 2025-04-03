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
import com.app.dolt.ui.signup.SignUpActivity


/**
 * Actividad encargada de gestionar el inicio de sesión de los usuarios.
 * Permite al usuario autenticarse o registrarse.
 */
class LoginActivity : AppCompatActivity() {

    // Referencia al binding y al ViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    /**
     * Método llamado al crear la actividad.
     * Configura la interfaz y los eventos de inicio de sesión y registro.
     *
     * @param savedInstanceState : Estado anterior guardado (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Obtiene las preferencias compartidas
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

        // Crea el ViewModel usando el Factory
        val factory = LoginViewModelFactory(sharedPreferences)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        // Configura el evento al pulsar el botón de login
        binding.btnLogin.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Llamada al método de login en el ViewModel
                loginViewModel.login(username, password,
                    onSuccess = { token ->
                        Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                        Log.i("TOKEN: ", token)
                        navigateToMainActivity()
                    },
                    onError = { errorMessage ->
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(this, "Por favor, ingrese los datos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        // Configura el evento al pulsar el enlace de registro
        binding.registerLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    /**
     * Redirige al usuario a la pantalla principal (Feed de desafíos).
     */
    private fun navigateToMainActivity() {
        val intent = Intent(this, FeedCActivity::class.java)
        startActivity(intent)
        finish()  // Finaliza LoginActivity para evitar volver atrás
    }
}
