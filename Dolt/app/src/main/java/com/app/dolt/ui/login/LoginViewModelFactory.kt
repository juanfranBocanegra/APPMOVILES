package com.app.dolt.ui.login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Fábrica para crear instancias de [LoginViewModel] con sus dependencias.
 *
 * @property sharedPreferences : Referencia a las preferencias compartidas necesarias para el ViewModel.
 */
class LoginViewModelFactory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.NewInstanceFactory() {
    
    /**
     * Crea una nueva instancia de [LoginViewModel].
     *
     * @param modelClass : Clase del ViewModel a crear.
     * @return Instancia de [LoginViewModel].
     * @throws IllegalArgumentException : Si la clase no es [LoginViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
