package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.network.UserId
import com.example.skolskaplikacia.repository.SpravyRepository
import com.example.skolskaplikacia.uiStates.SpravyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpravyViewModel (
    private val spravyRepository: SpravyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SpravyUiState(spravyZoznam = listOf()))
    val uiState: StateFlow<SpravyUiState> = _uiState.asStateFlow()

    fun loadData(){
        viewModelScope.launch {
            val spravy = spravyRepository.getAllSpravy()
            _uiState.update { currentState ->
                currentState.copy(spravyZoznam = spravy)
            }
        }
    }
}

