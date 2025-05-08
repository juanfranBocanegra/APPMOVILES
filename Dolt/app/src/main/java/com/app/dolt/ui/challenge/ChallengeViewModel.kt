package com.app.dolt.ui.challenge

import android.content.Context
import com.app.dolt.repository.ChallengeRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dolt.model.Challenge
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log
import timber.log.Timber


/**
 * ViewModel encargado de gestionar la lógica y datos relacionados con los desafíos.
 * Recupera la lista de desafíos desde el repositorio y expone el estado mediante [StateFlow].
 */
class ChallengeViewModel : ViewModel() {
    private val repository = ChallengeRepository()

    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())

    /**
     * Flujo de datos que proporciona la lista de desafíos a la interfaz.
     * Se actualiza automáticamente cuando los datos cambian.
     */
    val challenges: StateFlow<List<Challenge>> = _challenges

    /**
     * Carga la lista de desafíos desde el repositorio.
     * En caso de error, lo registra en el log.
     */
    fun loadChallenges() {
        viewModelScope.launch {
            try {
                repository.refreshChallenges()
                _challenges.value = repository.getLocalChallenges()
            } catch (e: Exception) {
                Timber.tag("ERROR").i(e.toString())
            }
        }
    }

    suspend fun refreshChallenges(){
        repository.refreshChallenges()
    }
}
