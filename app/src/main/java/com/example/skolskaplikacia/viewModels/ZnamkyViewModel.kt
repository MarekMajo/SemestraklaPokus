package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.network.PoziadavkyNaPodpisZnamok
import com.example.skolskaplikacia.network.RetrofitClient
import com.example.skolskaplikacia.network.UserId
import com.example.skolskaplikacia.repository.DetiRepository
import com.example.skolskaplikacia.repository.OsobaRepository
import com.example.skolskaplikacia.repository.ZnamkyRepository
import com.example.skolskaplikacia.uiStates.VysledokPredmetu
import com.example.skolskaplikacia.uiStates.Znamka
import com.example.skolskaplikacia.uiStates.ZnamkyUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ZnamkyViewModel (
    private val znamkyRepository: ZnamkyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ZnamkyUiState(predmetyZiaka = listOf(), selectUser = 0))
    val uiState: StateFlow<ZnamkyUiState> = _uiState.asStateFlow()
    fun vytvorZnamkyVysledok(idZiaka: Int) {
        viewModelScope.launch {
                val predmety = znamkyRepository.getAllPredmety(idZiaka)
                val kategorie = znamkyRepository.getAllKategorie()
                val znamky = znamkyRepository.getAllZnamky()
                _uiState.update {it.copy(selectUser = idZiaka)}

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
                                            prepocteneZnamky.first().znamka,
                                            kategoria.vaha.toDouble(),
                                            znamka.podpis
                                        )
                                    )
                                }
                            }
                        } else {
                            znamkyKategoria.forEach { znamka ->
                                znamkyPrePredmet.add(
                                    Znamka(
                                        znamka.znamka,
                                        kategoria.vaha.toDouble(),
                                        znamka.podpis
                                    )
                                )
                            }
                        }
                    }
                    val sumaVahovanychZnamok = znamkyPrePredmet.sumOf { it.znamka * it.vaha }
                    val sumaVah = znamkyPrePredmet.sumOf { it.vaha }
                    val priemer = if (sumaVah != 0.0) sumaVahovanychZnamok / sumaVah else 0.0

                    VysledokPredmetu(predmet.predmet, predmet.predmetId , znamkyPrePredmet, String.format("%.2f", priemer))
                }
                _uiState.update {it.copy(predmetyZiaka = vysledok)}
            }
    }

    fun PodpisanieZnamok() {
        viewModelScope.launch {
            val userId = uiState.value.selectUser
            val predmety = znamkyRepository.getAllPredmety(userId)
            val kategorie = znamkyRepository.getAllKategorie()
            val vysledok = mutableListOf<Int>()
            for (predmet in predmety) {
                val kategoriePredmetu = kategorie.filter { it.predmetId == predmet.predmetId }
                vysledok.addAll(kategoriePredmetu.map { it.kategoriaId })
            }
            try {
                RetrofitClient.apiService.MobileGetPodpisanieZnamok(PoziadavkyNaPodpisZnamok(vysledok, userId))
            } catch (e: Exception) {
                println(e)
            }
        }
    }



}