package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import com.example.skolskaplikacia.uiStates.DochadzkaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DochadzkaViewModel (
) : ViewModel() {
    private val _uiState = MutableStateFlow(DochadzkaUiState())
    val uiState: StateFlow<DochadzkaUiState> = _uiState.asStateFlow()
}