package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.repository.OsobaRepository
import com.example.skolskaplikacia.uiStates.MenuUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(private val osobaRepository: OsobaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuUiState(meno = "", priezvisko = ""))
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()
    init {
        setUsername()
    }
    fun setUsername() {
        viewModelScope.launch {
            val osoba = osobaRepository.jePrihlaseny()
            if (osoba != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        meno = osoba.meno,
                        priezvisko = osoba.priezvisko
                    )
                }
            }
        }
    }
    fun toggleDropdown() {
        _uiState.update { currentState ->
            currentState.copy(isDropdownVisible = !currentState.isDropdownVisible)
        }
    }

    fun hideDropdown() {
        _uiState.update { currentState ->
            if (currentState.isDropdownVisible) {
                currentState.copy(isDropdownVisible = false)
            } else {
                currentState
            }
        }
    }
}