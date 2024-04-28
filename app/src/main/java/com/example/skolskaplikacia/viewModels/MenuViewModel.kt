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
        private val _uiState = MutableStateFlow(MenuUiState(meno = "", priezvisko = "",selectUser = 0 , blokyVDni = listOf(), zoznamDeti = listOf()))
        val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

        suspend fun setBlokovRozvrhu() {
            val osobaId = _uiState.value.selectUser
            val databaza = rozvrhRepository.getRozvrhOsoby(osobaId)
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

        fun resetUiState() {
            _uiState.update { currentState ->
                currentState.copy(
                    meno = "",
                    priezvisko = "",
                    selectUser = 0,
                    zoznamDeti = listOf(),
                    blokyVDni = listOf()
                )
            }
        }

        fun LoadData() {
            resetUiState()
            viewModelScope.launch {
                val deti = detiRepository.getAllDeti()
                if (deti.isNotEmpty()) {
                    var selectUser = deti.get(0).dietaId
                    _uiState.update { currentState ->
                        currentState.copy(selectUser = selectUser, zoznamDeti = deti)
                    }
                    setBlokovRozvrhu()
                    try {
                        for (item in deti) {
                            val response =RetrofitClient.apiService.getUserRozvrh(UserId(item.dietaId)).list
                            val existujucaDatabaza = rozvrhRepository.getAllRozvrh()
                            val (toAdd, toDelete, toUpdate) = PorovnajDatabazuRozvrh(
                                response,
                                existujucaDatabaza,
                                item.dietaId
                            )
                            PridajDoRozvrhu(toAdd)
                            OdstranZRozvrhu(toDelete)
                            UpravVRozvrhu(toUpdate, item.dietaId)

                        }
                    }  catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    val osoba = osobaRepository.jePrihlaseny()
                    if (osoba != null) {
                        _uiState.update { currentState ->
                            currentState.copy(selectUser = osoba.osobaId)
                        }
                        setBlokovRozvrhu()
                        val response =RetrofitClient.apiService.getUserRozvrh(UserId(osoba.osobaId)).list
                        val existujucaDatabaza = rozvrhRepository.getAllRozvrh()
                        val (toAdd, toDelete, toUpdate) = PorovnajDatabazuRozvrh(
                            response,
                            existujucaDatabaza,
                            osoba.osobaId
                        )
                        PridajDoRozvrhu(toAdd)
                        OdstranZRozvrhu(toDelete)
                        UpravVRozvrhu(toUpdate, osoba.osobaId)

                    }
                }
                setBlokovRozvrhu()
            }
        }

        fun ChangeSelectUser(newSelectUserID: Int) {
            _uiState.update { currentState ->
                currentState.copy(selectUser = newSelectUserID)
            }
            viewModelScope.launch {
                setBlokovRozvrhu()
            }
        }

        fun PorovnajDatabazuRozvrh(serverRozvrh: List<RozvrhTuple>, dbRozvrh: List<Rozvrh>, osobaId: Int): Triple<List<RozvrhTuple>, List<Int>, List<RozvrhTuple>> {
            val filteredDbRozvrh = dbRozvrh.filter { it.osobaId == osobaId }

            val serverKeys = serverRozvrh.map { it.rozvrhId to it.osobaId }.toSet()
            val dbKeys = filteredDbRozvrh.map { it.rozvrhId to it.osobaId }.toSet()

            val toAdd = serverRozvrh.filter { (it.rozvrhId to it.osobaId) !in dbKeys }
            val toDelete = filteredDbRozvrh.filter { (it.rozvrhId to it.osobaId) !in serverKeys }.map { it.rozvrhId }
            val toUpdate = serverRozvrh.filter { (it.rozvrhId to it.osobaId) in dbKeys }.filter { tuple ->
                val dbVersion = filteredDbRozvrh.first { it.rozvrhId == tuple.rozvrhId && it.osobaId == tuple.osobaId }
                tuple.den != dbVersion.den || tuple.blok != dbVersion.blok ||
                        tuple.predmet != dbVersion.predmet || tuple.trieda != dbVersion.ucebna
            }

            return Triple(toAdd, toDelete, toUpdate)
        }



        //fun PorovnajDatabazuRozvrh(serverRozvrh: List<RozvrhTuple>, dbRozvrh: List<Rozvrh>): Triple<List<RozvrhTuple>, List<Int>, List<RozvrhTuple>> {
        //    val serverIds = serverRozvrh.map { it.rozvrhId }.toSet()
        //    val dbIds = dbRozvrh.map { it.rozvrhId }.toSet()
//
        //    val toAdd = serverRozvrh.filter { it.rozvrhId !in dbIds }
        //    val toDelete = dbRozvrh.filter { it.rozvrhId !in serverIds }.map { it.rozvrhId }
        //    val toUpdate = serverRozvrh.filter { it.rozvrhId in dbIds }.filter { tuple ->
        //        val dbVersion = dbRozvrh.first { it.rozvrhId == tuple.rozvrhId }
        //        tuple.den != dbVersion.den || tuple.blok != dbVersion.blok ||
        //                tuple.predmet != dbVersion.predmet || tuple.trieda != dbVersion.ucebna
        //    }
//
        //    return Triple(toAdd, toDelete, toUpdate)
        //}
        suspend fun PridajDoRozvrhu(zoznam: List<RozvrhTuple>){
            for (item in zoznam) {
                rozvrhRepository.insertRozvrh(Rozvrh(
                    rozvrhId = item.rozvrhId,
                    osobaId = item.osobaId,
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