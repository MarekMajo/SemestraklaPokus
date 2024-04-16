package com.example.skolskaplikacia
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(slovo = "ahoj"))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
}