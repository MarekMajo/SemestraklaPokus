    package com.example.skolskaplikacia.viewModels

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.skolskaplikacia.network.LoginData
    import com.example.skolskaplikacia.network.RetrofitClient
    import com.example.skolskaplikacia.network.RozvrhTuple
    import com.example.skolskaplikacia.network.UserId
    import com.example.skolskaplikacia.repository.OsobaRepository
    import com.example.skolskaplikacia.repository.RozvrhRepository
    import com.example.skolskaplikacia.uiStates.MenuUiState
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.update
    import kotlinx.coroutines.launch

    class MenuViewModel(private val osobaRepository: OsobaRepository,
        private val rozvrhRepository: RozvrhRepository) : ViewModel() {
        private val _uiState = MutableStateFlow(MenuUiState(meno = "", priezvisko = ""))
        val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                val osoba = osobaRepository.jePrihlaseny()
                if (osoba != null) {
                    try {
                        val response = RetrofitClient.apiService.getUserRozvrh(UserId(osoba.osobaId))
                        println(response.list)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
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
    }