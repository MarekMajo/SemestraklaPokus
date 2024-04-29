package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import com.example.skolskaplikacia.uiStates.ZnamkyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ZnamkyViewModel (
) : ViewModel() {
    private val _uiState = MutableStateFlow(ZnamkyUiState())
    val uiState: StateFlow<ZnamkyUiState> = _uiState.asStateFlow()
}