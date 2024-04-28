package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.databaza.Deti
import com.example.skolskaplikacia.databaza.Osoba
import com.example.skolskaplikacia.network.DetiTuple
import com.example.skolskaplikacia.network.LoginData
import com.example.skolskaplikacia.network.RetrofitClient
import com.example.skolskaplikacia.network.UserId
import com.example.skolskaplikacia.repository.DetiRepository
import com.example.skolskaplikacia.repository.OsobaRepository
import com.example.skolskaplikacia.repository.RozvrhRepository
import com.example.skolskaplikacia.uiStates.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel pre správu prihlasovania používateľov.
 * Udržiava stav prihlasovania a poskytuje funkcie pre prihlásenie, odhlásenie.
 *
 * @property osobaRepository Prístup k databázovým operáciám pre osoby.
 */
class LoginViewModel(
    private val osobaRepository: OsobaRepository,
    private val rozvrhRepository: RozvrhRepository,
    private val detiRepository: DetiRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState(userID = 0, userName = "", userPassword = ""))
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        /**
         * Pri inicializácii kontroluje existenciu prihláseného používateľa v databáze
         * a aktualizuje uiState na základe dostupných údajov.
         */
       viewModelScope.launch {
           val existingUser = osobaRepository.jePrihlaseny()
           if (existingUser != null) {
               _uiState.update { currentState ->
                   currentState.copy(userID = existingUser.osobaId)
               }
           }
       }
    }

    /**
     * Funkcia na odhlásenie používateľa. Vymaže používateľa z databáze a resetuje stav prihlásenia.
     */
    fun logout() {
        val currentUserId = uiState.value.userID
        viewModelScope.launch {
            osobaRepository.deleteOsoba(Osoba(osobaId = currentUserId))
            rozvrhRepository.deleteAllRozvrhy()
            _uiState.update { currentState ->
                currentState.copy(userID = 0, userName = "", userPassword = "")
            }
        }
    }

    /**
     * Aktualizuje prihlasovacie meno používateľa v stave.
     * @param meno Nové prihlasovacie meno používateľa.
     */
    fun updateUserName(meno: String) {
        _uiState.update { currentState ->
            currentState.copy(userName = meno)
        }
    }

    /**
     * Aktualizuje prihlasovacie heslo používateľa v stave.
     * @param heslo Nové prihlasovacie heslo používateľa.
     */
    fun updateUserPassword(heslo: String) {
        _uiState.update { currentState ->
            currentState.copy(userPassword = heslo)
        }
    }

    /**
     * Funkcia na prihlásenie používateľa. Overí údaje a pri úspechu aktualizuje stav, inak nastaví chybový stav.
     * @param meno Prihlasovacie meno používateľa.
     * @param heslo Prihlasovacie heslo používateľa.
     */
    fun login(meno: String, heslo: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.loginUser(LoginData(meno, heslo))
                if (response.result > 0) {
                    val udaje = RetrofitClient.apiService.getUserName(UserId(response.result))
                    val deti = RetrofitClient.apiService.getUserDeti(UserId(response.result))
                    val id = osobaRepository.insertOsoba(Osoba(osobaId = response.result, meno = udaje.meno, priezvisko = udaje.priezvisko))
                    pridatDetiDoDatabazy(deti.list , id.toInt())
                    _uiState.update { currentState ->
                        currentState.copy(userID = response.result, userName = meno)
                    }
                } else {
                    _uiState.update { currentState ->
                        currentState.copy(userID = -1)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { currentState ->
                    currentState.copy(userID = -2)
                }
            }
        }
    }

    suspend fun pridatDetiDoDatabazy(zoznam: List<DetiTuple>, rodicID: Int) {
        for (item in zoznam) {
            detiRepository.insertDieta(Deti(
                rodic = rodicID,
                dietaId = item.dietaId,
                meno = item.meno,
                priezvisko = item.priezvisko
                ))
        }
    }
}
