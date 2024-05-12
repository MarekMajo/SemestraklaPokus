package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.network.blokyDna
import com.example.skolskaplikacia.network.dochadzkaDen
import com.example.skolskaplikacia.repository.DochadzkaRepository
import com.example.skolskaplikacia.uiStates.DochadzkaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DochadzkaViewModel (
    private val dochadzkaRepository: DochadzkaRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DochadzkaUiState(selectUser = 0, dochadzka = listOf(), celkoveChybanie = 0, pocetOspravedlnenych = 0, pocetNeospravedlnenych = 0))
    val uiState: StateFlow<DochadzkaUiState> = _uiState.asStateFlow()

    fun loadData(userId: Int) {
        viewModelScope.launch {
            var celkoveChybanie = 0
            var pocetOspravedlnenych = 0
            var pocetNeospravedlnenych = 0
            val dbDochadzkaList = dochadzkaRepository.getDochadzka(userId)
            val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val dochadzkaList = dbDochadzkaList.groupBy { LocalDate.parse(it.den, dateFormatter) }
                .toSortedMap(compareByDescending { it })
                .map { entry ->
                    dochadzkaDen(
                        datum = entry.key.format(dateFormatter),
                        bloky = entry.value.map {
                            celkoveChybanie++
                            if (it.ospravedlnene == 1) pocetOspravedlnenych++ else if (it.ospravedlnene == 2) pocetNeospravedlnenych++
                            blokyDna(
                                block = it.block,
                                typ = it.typ,
                                poznamka = it.poznamka,
                                ospravedlnene = it.ospravedlnene,
                                id_dochadza = it.id_dochadza
                            )
                        }
                    )
                }

            _uiState.update { it.copy(
                selectUser = userId,
                dochadzka = dochadzkaList,
                celkoveChybanie = celkoveChybanie,
                pocetOspravedlnenych = pocetOspravedlnenych,
                pocetNeospravedlnenych = pocetNeospravedlnenych
            )}
        }
    }
}