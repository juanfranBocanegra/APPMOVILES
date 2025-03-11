package com.app.dolt.ui.signup


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.app.dolt.R
import com.app.dolt.databinding.ActivitySignupBinding
import com.app.dolt.ui.challenge.FeedCActivity
import com.app.dolt.ui.login.LoginActivity
import com.app.dolt.ui.login.LoginViewModel
import com.app.dolt.ui.login.LoginViewModelFactory
import com.app.dolt.ui.signup.SignUpViewModel
import com.app.dolt.ui.signup.SignUpViewModelFactory

class SignUpActivity : AppCompatActivity() {

    // Referencia al binding y al ViewModel
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignUpViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        // Obtén SharedPreferences
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

        // Crea el ViewModel usando el Factory
        val signupFactory = SignUpViewModelFactory()
        val loginFactory = LoginViewModelFactory(sharedPreferences)
        signupViewModel = ViewModelProvider(this, signupFactory).get(SignUpViewModel::class.java)
        loginViewModel = ViewModelProvider(this,loginFactory).get(LoginViewModel::class.java)

        // Escuchar el clic del botón de signup
        binding.btnSignUp.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val password2 = binding.password2EditText.text.toString()

            if (username.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) {

                if (password == password2) {

                    signupViewModel.signup(username, name, password, password2,
                        onSuccess = {

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
                                })


                        },
                        onError = { errorMessage ->
                            // Mostrar mensaje de error si algo salió mal
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )

                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Si los campos están vacíos
                Toast.makeText(this, "Por favor, ingrese los datos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    // Navegar a la actividad principal
    private fun navigateToMainActivity() {
        val intent = Intent(this, FeedCActivity::class.java)
        startActivity(intent)
        finish()  // Finalizar SignUpActivity para que no se pueda volver atrás
    }
}
