package com.app.dolt.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.dolt.R
import com.app.dolt.api.RetrofitClient
import com.app.dolt.databinding.ActivityLoginBinding
import com.app.dolt.model.GoogleLoginRequest
import com.app.dolt.ui.challenge.FeedCActivity
import com.app.dolt.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Actividad encargada de gestionar el inicio de sesión de los usuarios.
 * Permite al usuario autenticarse o registrarse.
 */
class LoginActivity : AppCompatActivity() {

    // Referencia al binding y al ViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager
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

        auth = Firebase.auth

        // Instantiate a Google sign-in request
        val googleIdOption = GetGoogleIdOption.Builder()
            // Your server's client ID, not your Android client ID.
            .setServerClientId(getString(R.string.default_web_client_id))
            // Only show accounts previously used to sign in.
            .setFilterByAuthorizedAccounts(true)
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        // Obtiene las preferencias compartidas
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)

        // Crea el ViewModel usando el Factory
        val factory = LoginViewModelFactory(sharedPreferences)
        loginViewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        // Configura el evento al pulsar el botón de login
        binding.loginButton.setOnClickListener {
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

        binding.googleLoginButton.setOnClickListener {
            launchGoogleSignIn()
        }

        // Configura el evento al pulsar el enlace de registro
        binding.registerLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
    }

    private fun launchGoogleSignIn() {
        credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false) // Muestra todas las cuentas
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )
                handleSignIn(result.credential)
            } catch (e: Exception) {
                Log.e("LoginActivity", "Sign-in failed", e)
                Toast.makeText(this@LoginActivity, "Error de autenticación", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w("AAAA", "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", Context.MODE_PRIVATE)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AAA", "signInWithCredential:success")
                    val user = auth.currentUser

                    user?.getIdToken(true)?.addOnCompleteListener { tokenTask ->
                        if (tokenTask.isSuccessful) {
                            val firebaseIdToken = tokenTask.result?.token
                            lifecycleScope.launch {

                                Timber.e(idToken)

                                val response = RetrofitClient.apiService.googleLogin(
                                    GoogleLoginRequest(
                                        firebaseIdToken.toString()
                                    )
                                )

                                if (response.isSuccessful) {
                                    val token = response.body()?.access
                                    val refresh = response.body()?.refresh
                                    if (token != null && refresh != null) {
                                        sharedPreferences.edit().putString("ACCESS_TOKEN", token).apply()

                                        sharedPreferences.edit().putString("REFRESH_TOKEN",refresh).apply()

                                        val email = user.email ?: ""
                                        val username = email.substringBefore("@") + "_google"
                                        sharedPreferences.edit().putString("USERNAME", username).apply()

                                        navigateToMainActivity()
                                    }
                                }
                            }
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w("AAA", "signInWithCredential:failure", task.exception)
                        }
                    }
                }
            }
    }

    /**
     * Redirige al usuario a la pantalla principal (Feed de desafíos).
     */
    fun navigateToMainActivity() {
        val intent = Intent(this, FeedCActivity::class.java)
        startActivity(intent)
        finish()  // Finaliza LoginActivity para evitar volver atrás
    }
}
