package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.databaza.Spravy
import com.example.skolskaplikacia.repository.SpravyRepository
import com.example.skolskaplikacia.uiStates.SpravyUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpravyViewModel (
    private val spravyRepository: SpravyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SpravyUiState(spravyZoznam = listOf(), selectUser = 0))
    val uiState: StateFlow<SpravyUiState> = _uiState.asStateFlow()

    fun loadData(userId: Int){
        viewModelScope.launch {
            val spravy = spravyRepository.getUserSpravy(userId)
            _uiState.update { it.copy(spravyZoznam = spravy, selectUser = userId)}
        }
    }
}

