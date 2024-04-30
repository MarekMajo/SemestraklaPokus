package com.example.skolskaplikacia.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skolskaplikacia.databaza.Kategorie
import com.example.skolskaplikacia.databaza.Predmety
import com.example.skolskaplikacia.databaza.Znamky
import com.example.skolskaplikacia.network.RetrofitClient
import com.example.skolskaplikacia.network.UserId
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


    //fun loadData() {
    //    viewModelScope.launch {
    //        val deti = detiRepository.getAllDeti()
    //        if (deti.isNotEmpty()) {
    //            try {
    //                znamkyRepository.deleteAllPredmety()
    //                for (dieta in deti) {
    //                    val znamkyRequest = RetrofitClient.apiService.MobileGetZnamky(UserId(dieta.dietaId)).list
    //                    for (predmet in znamkyRequest) {
    //                        val predmetId = znamkyRepository.vlozitPredmet(Predmety(predmet = predmet.predmet, osobaId = dieta.dietaId))
    //                        for (kategoria in predmet.kategorie) {
    //                            val kategoriaId = znamkyRepository.vlozitKategoriu(Kategorie(predmetId = predmetId.toInt(), kategoriaId = kategoria.kategoria_id,
    //                                max_body = kategoria.max_body, nazov = kategoria.kategoriaNazov, vaha = kategoria.vaha ))
    //                            for(znamka in kategoria.znamkyPodpis) {
    //                                znamkyRepository.vlozitZnamka(Znamky(kategoriaId = kategoriaId.toInt(), znamka = znamka.znamka, podpis = 1))
    //                            }
    //                            for(znamka in kategoria.znamkyNePodpis) {
    //                                znamkyRepository.vlozitZnamka(Znamky(kategoriaId = kategoriaId.toInt(), znamka = znamka.znamka, podpis = 0))
    //                            }
    //                        }
    //                    }
    //                }
    //            } catch (e: Exception) {
    //                e.printStackTrace()
    //            }
    //        } else {
    //            znamkyRepository.deleteAllPredmety()
    //            val osoba = osobaRepository.jePrihlaseny()
    //            if (osoba != null) {
    //                val znamkyRequest = RetrofitClient.apiService.MobileGetZnamky(UserId(osoba.osobaId)).list
    //                for (predmet in znamkyRequest) {
    //                    val predmetId = znamkyRepository.vlozitPredmet(Predmety(predmet = predmet.predmet, osobaId = osoba.osobaId))
    //                    for (kategoria in predmet.kategorie) {
    //                        val kategoriaId = znamkyRepository.vlozitKategoriu(Kategorie(predmetId = predmetId.toInt(), kategoriaId = kategoria.kategoria_id,
    //                            max_body = kategoria.max_body, nazov = kategoria.kategoriaNazov, vaha = kategoria.vaha ))
    //                        for(znamka in kategoria.znamkyPodpis) {
    //                            znamkyRepository.vlozitZnamka(Znamky(kategoriaId = kategoriaId.toInt(), znamka = znamka.znamka, podpis = 1))
    //                        }
    //                        for(znamka in kategoria.znamkyNePodpis) {
    //                            znamkyRepository.vlozitZnamka(Znamky(kategoriaId = kategoriaId.toInt(), znamka = znamka.znamka, podpis = 0))
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //    }
    //}
}