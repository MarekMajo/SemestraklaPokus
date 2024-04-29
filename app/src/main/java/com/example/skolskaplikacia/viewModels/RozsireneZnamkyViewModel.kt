package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import com.example.skolskaplikacia.uiStates.RozsireneZnamkyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RozsireneZnamkyViewModel (
) : ViewModel() {
    private val _uiState = MutableStateFlow(RozsireneZnamkyUiState())
    val uiState: StateFlow<RozsireneZnamkyUiState> = _uiState.asStateFlow()
}