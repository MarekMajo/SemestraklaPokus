package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.repository.DetiRepository
import com.example.skolskaplikacia.repository.OsobaRepository
import com.example.skolskaplikacia.repository.ZnamkyRepository
import com.example.skolskaplikacia.uiStates.ZnamkyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ZnamkyViewModel (
    private val osobaRepository: OsobaRepository,
    private val detiRepository: DetiRepository,
    private val znamkyRepository: ZnamkyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ZnamkyUiState())
    val uiState: StateFlow<ZnamkyUiState> = _uiState.asStateFlow()
    var b = true
    fun vytvorZnamkyVysledok() {
        data class Znamka(val znamka: Int, val vaha: Double)
        data class VysledokPredmetu(val predmet: String, val znamky: List<Znamka>, val priemer: String)

        viewModelScope.launch {
            if (b) {
                b = false
                val predmety = znamkyRepository.getAllPredmety()
                val kategorie = znamkyRepository.getAllKategorie()
                val znamky = znamkyRepository.getAllZnamky()

                val vysledok = predmety.map { predmet ->
                    val znamkyPrePredmet = mutableListOf<Znamka>()
                    val kategoriePrePredmet = kategorie.filter { it.predmetId == predmet.predmetId }

                    for (kategoria in kategoriePrePredmet) {
                        val znamkyKategoria =
                            znamky.filter { it.kategoriaId == kategoria.kategoriaId }

                        if (kategoria.typ != "znamka") {
                            znamkyKategoria.forEach { znamka ->
                                val prepocteneZnamky = znamkyRepository.getZnamkuZprepoctu(
                                    kategoria.kategoriaId,
                                    znamka.znamka
                                )
                                if (prepocteneZnamky.isNotEmpty()) {
                                    znamkyPrePredmet.add(
                                        Znamka(
                                            prepocteneZnamky[0].znamka,
                                            kategoria.vaha.toDouble()
                                        )
                                    )
                                }
                            }
                        } else {
                            znamkyKategoria.forEach { znamka ->
                                znamkyPrePredmet.add(
                                    Znamka(
                                        znamka.znamka,
                                        kategoria.vaha.toDouble()
                                    )
                                )
                            }
                        }
                    }
                    val sumaVahovanychZnamok = znamkyPrePredmet.sumOf { it.znamka * it.vaha }
                    val sumaVah = znamkyPrePredmet.sumOf { it.vaha }
                    val priemer = if (sumaVah != 0.0) sumaVahovanychZnamok / sumaVah else 0.0

                    VysledokPredmetu(predmet.predmet, znamkyPrePredmet, String.format("%.2f", priemer))
                }

                vysledok.forEach { println(it) }
                println("---------")
            }
        }
    }
}