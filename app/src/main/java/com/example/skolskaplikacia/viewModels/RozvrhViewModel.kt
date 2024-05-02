package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.repository.RozvrhRepository
import com.example.skolskaplikacia.uiStates.BlokRozvrhu
import com.example.skolskaplikacia.uiStates.RozvrhUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RozvrhViewModel (
    private val rozvrhRepository: RozvrhRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RozvrhUiState(rozvrhZoznam = listOf(), selectUser = 0))
    val uiState: StateFlow<RozvrhUiState> = _uiState.asStateFlow()

    fun loadData(userId: Int) {
        viewModelScope.launch {
            val rozvrh = rozvrhRepository.getRozvrhOsoby(userId)
            val blokRozvrhu = mutableListOf<BlokRozvrhu>()
            rozvrh.forEach { item ->
                if (item.osobaId == userId) {
                    blokRozvrhu.add(BlokRozvrhu(
                        item.den,
                        item.blok,
                        texty = listOf(item.predmet, item.ucebna)
                    ))
                }
            }
            _uiState.update { it.copy(rozvrhZoznam = blokRozvrhu, selectUser = userId) }
        }
    }

}