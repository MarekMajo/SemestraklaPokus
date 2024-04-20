package com.example.skolskaplikacia
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.network.LoginData
import com.example.skolskaplikacia.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState(userID = 0))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    fun logout() {
        _uiState.value = AppUiState(userID = 0)
    }

    fun login(meno: String, heslo: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.loginUser(LoginData(meno, heslo))
                if (response.result > 0) {
                    _uiState.value = AppUiState(userID = response.result)
                } else {
                    _uiState.value = AppUiState(userID = -1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = AppUiState(userID = -2)
            }
        }
    }
}