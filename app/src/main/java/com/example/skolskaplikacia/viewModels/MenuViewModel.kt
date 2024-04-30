    package com.example.skolskaplikacia.viewModels

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.skolskaplikacia.databaza.Kategorie
    import com.example.skolskaplikacia.databaza.Predmety
    import com.example.skolskaplikacia.databaza.Rozvrh
    import com.example.skolskaplikacia.databaza.Spravy
    import com.example.skolskaplikacia.databaza.Znamky
    import com.example.skolskaplikacia.network.RetrofitClient
    import com.example.skolskaplikacia.network.RozvrhTuple
    import com.example.skolskaplikacia.network.SpravyTuple
    import com.example.skolskaplikacia.network.UserId
    import com.example.skolskaplikacia.repository.DetiRepository
    import com.example.skolskaplikacia.repository.OsobaRepository
    import com.example.skolskaplikacia.repository.RozvrhRepository
    import com.example.skolskaplikacia.repository.SpravyRepository
    import com.example.skolskaplikacia.repository.ZnamkyRepository
    import com.example.skolskaplikacia.uiStates.BlokTextu
    import com.example.skolskaplikacia.uiStates.MenuUiState
    import kotlinx.coroutines.async
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.flow.asStateFlow
    import kotlinx.coroutines.flow.update
    import kotlinx.coroutines.launch
    import java.time.LocalDate

    class MenuViewModel(
        private val osobaRepository: OsobaRepository,
        private val rozvrhRepository: RozvrhRepository,
        private val detiRepository: DetiRepository,
        private val spravyRepository: SpravyRepository,
        private val znamkyRepository: ZnamkyRepository
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

        fun PovolReaload(enable: Boolean) {
            _uiState.update { it.copy(reload = enable) }
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
            viewModelScope.launch {
                if (_uiState.value.reload) {
                    resetUiState()
                    _uiState.update { it.copy(reload = false) }
                    val deti = detiRepository.getAllDeti()
                    if (deti.isNotEmpty()) {
                        _uiState.update { currentState ->
                            currentState.copy(selectUser = deti[0].dietaId, zoznamDeti = deti)
                        }
                        setBlokovRozvrhu()
                        try {
                            for (item in deti) {
                                val rozvrhRequest = async { RetrofitClient.apiService.getUserRozvrh(UserId(item.dietaId)).list }
                                val spravyRequest = async { RetrofitClient.apiService.MobileGetSpravy(UserId(item.dietaId)).list }
                                val znamkyRequest = async { RetrofitClient.apiService.MobileGetZnamky(UserId(item.dietaId)).list }
                                val existujucaDatabazaRequest = async { rozvrhRepository.getAllRozvrh() }
                                val existujuceSpravyRequest = async { spravyRepository.getAllSpravy() }

                                val rozvrh = rozvrhRequest.await()
                                val spravy = spravyRequest.await()
                                val znamky = znamkyRequest.await()

                                val existujuciRozvrh = existujucaDatabazaRequest.await()
                                val existujuceSpravy = existujuceSpravyRequest.await()
                                val (toAddSpravy, toDeleteSpravy) = PorovnajSpravy(
                                    spravy,
                                    existujuceSpravy,
                                    item.dietaId
                                )
                                PridajDoSpravy(toAddSpravy)
                                OdstranZSpravy(toDeleteSpravy)

                                val (toAddRozrvrh, toDeleteRozvrh, toUpdateRozvrh) = PorovnajDatabazuRozvrh(
                                    rozvrh,
                                    existujuciRozvrh,
                                    item.dietaId
                                )
                                PridajDoRozvrhu(toAddRozrvrh)
                                OdstranZRozvrhu(toDeleteRozvrh)
                                UpravVRozvrhu(toUpdateRozvrh, item.dietaId)

                                znamkyRepository.deletePredmety(item.dietaId)
                                for (predmet in znamky) {
                                    val predmetId = znamkyRepository.vlozitPredmet(Predmety(predmet = predmet.predmet, osobaId = item.dietaId))
                                    for (kategoria in predmet.kategorie) {
                                        val kategoriaId = znamkyRepository.vlozitKategoriu(
                                            Kategorie(predmetId = predmetId.toInt(), kategoriaId = kategoria.kategoria_id,
                                            max_body = kategoria.max_body, nazov = kategoria.kategoriaNazov, vaha = kategoria.vaha )
                                        )
                                        for(znamka in kategoria.znamkyPodpis) {
                                            znamkyRepository.vlozitZnamka(Znamky(kategoriaId = kategoriaId.toInt(), znamka = znamka.znamka, podpis = 1))
                                        }
                                        for(znamka in kategoria.znamkyNePodpis) {
                                            znamkyRepository.vlozitZnamka(Znamky(kategoriaId = kategoriaId.toInt(), znamka = znamka.znamka, podpis = 0))
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    else {
                        val osoba = osobaRepository.jePrihlaseny()
                        if (osoba != null) {
                            _uiState.update { currentState ->
                                currentState.copy(selectUser = osoba.osobaId)
                            }
                            setBlokovRozvrhu()
                            try {
                                val rozvrhRequest =
                                    async { RetrofitClient.apiService.getUserRozvrh(UserId(osoba.osobaId)).list }
                                val spravyRequest =
                                    async { RetrofitClient.apiService.MobileGetSpravy(UserId(osoba.osobaId)).list }
                                val znamkyRequest =
                                    async { RetrofitClient.apiService.MobileGetZnamky(UserId(osoba.osobaId)).list }
                                val existujucaDatabazaRequest =
                                    async { rozvrhRepository.getAllRozvrh() }
                                val existujuceSpravyRequest =
                                    async { spravyRepository.getAllSpravy() }

                                val rozvrh = rozvrhRequest.await()
                                val spravy = spravyRequest.await()
                                val znamky = znamkyRequest.await()

                                val existujuciRozvrh = existujucaDatabazaRequest.await()
                                val existujuceSpravy = existujuceSpravyRequest.await()

                                val (toAddSpravy, toDeleteSpravy) = PorovnajSpravy(
                                    spravy,
                                    existujuceSpravy,
                                    osoba.osobaId
                                )
                                PridajDoSpravy(toAddSpravy)
                                OdstranZSpravy(toDeleteSpravy)

                                val (toAdd, toDelete, toUpdate) = PorovnajDatabazuRozvrh(
                                    rozvrh,
                                    existujuciRozvrh,
                                    osoba.osobaId
                                )
                                PridajDoRozvrhu(toAdd)
                                OdstranZRozvrhu(toDelete)
                                UpravVRozvrhu(toUpdate, osoba.osobaId)

                                znamkyRepository.deletePredmety(osoba.osobaId)
                                for (predmet in znamky) {
                                    val predmetId = znamkyRepository.vlozitPredmet(
                                        Predmety(
                                            predmet = predmet.predmet,
                                            osobaId = osoba.osobaId
                                        )
                                    )
                                    for (kategoria in predmet.kategorie) {
                                        val kategoriaId = znamkyRepository.vlozitKategoriu(
                                            Kategorie(
                                                predmetId = predmetId.toInt(),
                                                kategoriaId = kategoria.kategoria_id,
                                                max_body = kategoria.max_body,
                                                nazov = kategoria.kategoriaNazov,
                                                vaha = kategoria.vaha
                                            )
                                        )
                                        for (znamka in kategoria.znamkyPodpis) {
                                            znamkyRepository.vlozitZnamka(
                                                Znamky(
                                                    kategoriaId = kategoriaId.toInt(),
                                                    znamka = znamka.znamka,
                                                    podpis = 1
                                                )
                                            )
                                        }
                                        for (znamka in kategoria.znamkyNePodpis) {
                                            znamkyRepository.vlozitZnamka(
                                                Znamky(
                                                    kategoriaId = kategoriaId.toInt(),
                                                    znamka = znamka.znamka,
                                                    podpis = 0
                                                )
                                            )
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    setBlokovRozvrhu()
                }
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

        fun PorovnajSpravy(serverSpravy: List<SpravyTuple>, dbSpravy: List<Spravy>, osobaId: Int): Pair <List<SpravyTuple>, List<Int>>  {
            val filteredDbSpravy = dbSpravy.filter { it.osobaId == osobaId }
            val serverKeys = serverSpravy.map { it.spravaId to it.osobaId }.toSet()
            val dbKeys = filteredDbSpravy.map { it.spravaId to it.osobaId }.toSet()
            val toAdd = serverSpravy.filter { (it.spravaId to it.osobaId) !in dbKeys }
            val toDelete = filteredDbSpravy.filter { (it.spravaId to it.osobaId) !in serverKeys }.map { it.spravaId }
            return Pair(toAdd, toDelete)
        }
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
        suspend fun PridajDoSpravy(zoznam: List<SpravyTuple>){
            for (item in zoznam) {
                spravyRepository.insertOsoba(
                    Spravy(
                    osobaId = item.osobaId,
                        cas = item.cas,
                        datum = item.datum,
                        sprava = item.sprava,
                        spravaId = item.spravaId
                )
                )
            }
        }
        suspend fun OdstranZSpravy(zoznam: List<Int>){
            for (item in zoznam) {
                spravyRepository.deleteSprava(item)
            }
        }
    }