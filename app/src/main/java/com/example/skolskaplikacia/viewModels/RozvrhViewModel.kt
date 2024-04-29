package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import com.example.skolskaplikacia.uiStates.RozvrhUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RozvrhViewModel (
) : ViewModel() {
    private val _uiState = MutableStateFlow(RozvrhUiState())
    val uiState: StateFlow<RozvrhUiState> = _uiState.asStateFlow()
}