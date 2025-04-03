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


/**
 * Actividad encargada de gestionar el registro de nuevos usuarios.
 * Permite registrar un nuevo usuario y realiza el login automáticamente tras el registro.
 */
class SignUpActivity : AppCompatActivity() {

    // Referencia al binding y al ViewModel
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignUpViewModel
    private lateinit var loginViewModel: LoginViewModel

    /**
     * Método llamado al crear la actividad.
     * Configura la interfaz, los ViewModel y los eventos de registro.
     *
     * @param savedInstanceState : Estado anterior guardado (si existe).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa el binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)

        // Obtiene las preferencias compartidas
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

        // Crea el ViewModel usando sus Factory correspondientes
        val signupFactory = SignUpViewModelFactory()
        val loginFactory = LoginViewModelFactory(sharedPreferences)
        signupViewModel = ViewModelProvider(this, signupFactory).get(SignUpViewModel::class.java)
        loginViewModel = ViewModelProvider(this,loginFactory).get(LoginViewModel::class.java)

        // Evento al pulsar el botón de registro
        binding.btnSignUp.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val name = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val password2 = binding.password2EditText.text.toString()

            if (username.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) {
                if (password == password2) {
                    signupViewModel.signup(username, name, password, password2,
                        onSuccess = {
                            // Si el registro es exitoso, realiza el login automático
                            loginViewModel.login(username, password,
                                onSuccess = { token ->
                                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()
                                    Log.i("TOKEN: ", token)
                                    navigateToMainActivity()
                                },
                                onError = { errorMessage ->
                                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                                })
                        },
                        onError = { errorMessage ->
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, ingrese los datos correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        // Enlace para ir a la pantalla de login
        binding.loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Redirige al usuario a la pantalla principal (Feed de desafíos) tras un registro exitoso.
     */
    private fun navigateToMainActivity() {
        val intent = Intent(this, FeedCActivity::class.java)
        startActivity(intent)
        finish()  // Finalizar SignUpActivity para evitar volver atrás
    }
}
