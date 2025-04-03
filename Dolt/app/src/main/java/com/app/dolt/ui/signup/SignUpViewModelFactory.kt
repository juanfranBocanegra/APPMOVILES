package com.app.dolt.ui.signup

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Fábrica para crear instancias de [SignUpViewModel].
 * Permite que la actividad de registro pueda usar el ViewModel.
 */
class SignUpViewModelFactory() : ViewModelProvider.NewInstanceFactory() {

    /**
     * Crea una nueva instancia de [SignUpViewModel].
     *
     * @param modelClass : Clase del ViewModel a crear.
     * @return Instancia de [SignUpViewModel].
     * @throws IllegalArgumentException : Si la clase no es [SignUpViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
