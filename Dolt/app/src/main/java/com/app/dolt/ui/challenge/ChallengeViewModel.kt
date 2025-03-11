package com.app.dolt.ui.challenge

import com.app.dolt.repository.ChallengeRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dolt.model.Challenge
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log

class ChallengeViewModel : ViewModel() {
    private val repository = ChallengeRepository()

    private val _challenges = MutableStateFlow<List<Challenge>>(emptyList())
    val challenges: StateFlow<List<Challenge>> = _challenges

    fun loadChallenges() {
        viewModelScope.launch {
            try {
                _challenges.value = repository.getChallenges()
            } catch (e: Exception) {
                Log.i("ERROR",e.toString())
            }
        }
    }
}
