    package com.example.skolskaplikacia.viewModels

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.skolskaplikacia.databaza.Rozvrh
    import com.example.skolskaplikacia.network.RetrofitClient
    import com.example.skolskaplikacia.network.RozvrhTuple
    import com.example.skolskaplikacia.network.UserId
    import com.example.skolskaplikacia.repository.DetiRepository
    import com.example.skolskaplikacia.repository.OsobaRepository
    import com.example.skolskaplikacia.repository.RozvrhRepository
    import com.example.skolskaplikacia.uiStates.BlokTextu
    import com.example.skolskaplikacia.uiStates.MenuUiState
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.update
    import kotlinx.coroutines.launch
    import java.time.LocalDate

    class MenuViewModel(
        private val osobaRepository: OsobaRepository,
        private val rozvrhRepository: RozvrhRepository,
        private val detiRepository: DetiRepository
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MenuUiState(meno = "", priezvisko = "", blokyVDni = listOf()))
        val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

        suspend fun setBlokovRozvrhu(osobaID: Int) {
            val databaza = rozvrhRepository.getRozvrhOsoby(osobaID)
            var den = LocalDate.now().dayOfWeek.value
            if (den > 5) den = 1
            val blokyVDni = databaza.filter { it.den == den }
                .map { BlokTextu(it.blok, listOf(it.predmet, it.ucebna)) }

            _uiState.update { currentState ->
                currentState.copy(blokyVDni = blokyVDni)
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

        fun LoadData() {
            viewModelScope.launch {
                val osoba = osobaRepository.jePrihlaseny()
                if (osoba != null) {
                    setBlokovRozvrhu(osoba.osobaId)
                    try {
                        val response = RetrofitClient.apiService.getUserRozvrh(UserId(osoba.osobaId)).list
                        val existujucaDatabaza = rozvrhRepository.getAllRozvrh()
                        val (toAdd, toDelete, toUpdate) = PorovnajDatabazuRozvrh(response, existujucaDatabaza)
                        PridajDoRozvrhu(toAdd, osoba.osobaId)
                        OdstranZRozvrhu(toDelete)
                        UpravVRozvrhu(toUpdate, osoba.osobaId)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    setBlokovRozvrhu(osoba.osobaId)
                }
            }
        }

        fun PorovnajDatabazuRozvrh(serverRozvrh: List<RozvrhTuple>, dbRozvrh: List<Rozvrh>): Triple<List<RozvrhTuple>, List<Int>, List<RozvrhTuple>> {
            val serverIds = serverRozvrh.map { it.rozvrhId }.toSet()
            val dbIds = dbRozvrh.map { it.rozvrhId }.toSet()

            val toAdd = serverRozvrh.filter { it.rozvrhId !in dbIds }
            val toDelete = dbRozvrh.filter { it.rozvrhId !in serverIds }.map { it.rozvrhId }
            val toUpdate = serverRozvrh.filter { it.rozvrhId in dbIds }.filter { tuple ->
                val dbVersion = dbRozvrh.first { it.rozvrhId == tuple.rozvrhId }
                tuple.den != dbVersion.den || tuple.blok != dbVersion.blok ||
                        tuple.predmet != dbVersion.predmet || tuple.trieda != dbVersion.ucebna
            }

            return Triple(toAdd, toDelete, toUpdate)
        }
        suspend fun PridajDoRozvrhu(zoznam: List<RozvrhTuple>, osobaID: Int){
            for (item in zoznam) {
                rozvrhRepository.insertRozvrh(Rozvrh(
                    rozvrhId = item.rozvrhId,
                    osobaId = osobaID,
                    blok = item.blok,
                    den = item.den,
                    predmet = item.predmet,
                    ucebna = item.trieda
                ))
            }
        }
        suspend fun OdstranZRozvrhu(zoznam: List<Int>){
            for (item in zoznam) {
                rozvrhRepository.deleteRozvrhByID(item)
            }
        }
        suspend fun UpravVRozvrhu(zoznam: List<RozvrhTuple>, osobaID: Int){
            for (item in zoznam) {
                rozvrhRepository.updateRozvrhByID(
                    item.blok,
                    item.den,
                    item.predmet,
                    item.trieda,
                    osobaID
                )
            }
        }
    }