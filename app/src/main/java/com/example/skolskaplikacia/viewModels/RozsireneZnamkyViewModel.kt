package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.foreignKeyCheck
import com.example.skolskaplikacia.repository.ZnamkyRepository
import com.example.skolskaplikacia.uiStates.Kategorie
import com.example.skolskaplikacia.uiStates.RozsireneZnamkyUiState
import com.example.skolskaplikacia.uiStates.jZnamka
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RozsireneZnamkyViewModel (
    private val znamkyRepository: ZnamkyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RozsireneZnamkyUiState(predmetId = 0, nazovPredmetu = "", zoznamKategorii = listOf(), priemer = ""))
    val uiState: StateFlow<RozsireneZnamkyUiState> = _uiState.asStateFlow()

    fun loadData(predmetId: Int) {
        viewModelScope.launch {
            val predmet = znamkyRepository.getPredmet(predmetId)
            val kategorie = znamkyRepository.getAllKategorie()
            val znamky = znamkyRepository.getAllZnamky()

            val kategoriePrePredmet = kategorie.filter { it.predmetId == predmetId }
            val zoznamKategorii = mutableListOf<Kategorie>()

            for (kategoria in kategoriePrePredmet) {
                val znamkyKategoria = znamky.filter { it.kategoriaId == kategoria.kategoriaId }
                val zoznamZnakom = mutableListOf<jZnamka>()

                for (znamka in znamkyKategoria) {
                    val znamkyNaPrepocet = znamkyRepository.getZnamkuZprepoctu(znamka.kategoriaId, znamka.znamka)
                    val prepocetZnamka = znamkyNaPrepocet.firstOrNull()?.znamka

                    zoznamZnakom.add(jZnamka(
                        znamka = znamka.znamka,
                        prepocet = prepocetZnamka,
                        podpisane = znamka.podpis
                    ))
                }

                val sumaVahovanychZnamok = zoznamZnakom.sumOf { znamka ->
                    val pouzitaZnamka = znamka.prepocet ?: znamka.znamka
                    pouzitaZnamka * kategoria.vaha.toDouble()
                }
                val sumaVah = zoznamZnakom.size * kategoria.vaha.toDouble()

                val priemerKategorie = if (sumaVah != 0.0) {
                    sumaVahovanychZnamok / sumaVah
                } else 0.0

                zoznamKategorii.add(Kategorie(
                    nazov = kategoria.nazov,
                    vaha = kategoria.vaha.toDouble(),
                    typ = kategoria.typ,
                    maxBody = kategoria.max_body?.toInt() ?: 0,
                    zoznamZnakom = zoznamZnakom,
                    priemer = String.format("%.2f", priemerKategorie)
                ))
            }

            val priemerPredmetu = if (zoznamKategorii.isNotEmpty()) {
                zoznamKategorii.sumOf { it.priemer.replace(",", ".").toDouble() * it.vaha } / zoznamKategorii.sumOf { it.vaha }
            } else 0.0

            _uiState.update { it.copy(
                nazovPredmetu = predmet[0].predmet,
                predmetId = predmetId,
                zoznamKategorii = zoznamKategorii,
                priemer = String.format("%.2f", priemerPredmetu)
            ) }
        }
    }
}