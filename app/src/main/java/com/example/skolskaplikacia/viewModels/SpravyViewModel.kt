package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import com.example.skolskaplikacia.uiStates.SpravyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SpravyViewModel (
) : ViewModel() {
    private val _uiState = MutableStateFlow(SpravyUiState())
    val uiState: StateFlow<SpravyUiState> = _uiState.asStateFlow()


}

