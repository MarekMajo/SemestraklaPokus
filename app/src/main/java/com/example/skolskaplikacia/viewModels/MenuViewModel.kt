    package com.example.skolskaplikacia.viewModels

    import android.content.Context
    import android.widget.Toast
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import com.example.skolskaplikacia.databaza.Dochadzka
    import com.example.skolskaplikacia.databaza.Kategorie
    import com.example.skolskaplikacia.databaza.Predmety
    import com.example.skolskaplikacia.databaza.Rozvrh
    import com.example.skolskaplikacia.databaza.Spravy
    import com.example.skolskaplikacia.databaza.ZnamkaNaPrepocet
    import com.example.skolskaplikacia.databaza.Znamky
    import com.example.skolskaplikacia.network.RetrofitClient
    import com.example.skolskaplikacia.network.RozvrhTuple
    import com.example.skolskaplikacia.network.SpravyTuple
    import com.example.skolskaplikacia.network.UserId
    import com.example.skolskaplikacia.network.ZnamkyTuple
    import com.example.skolskaplikacia.network.dochadzkaDen
    import com.example.skolskaplikacia.repository.DetiRepository
    import com.example.skolskaplikacia.repository.DochadzkaRepository
    import com.example.skolskaplikacia.repository.OsobaRepository
    import com.example.skolskaplikacia.repository.RozvrhRepository
    import com.example.skolskaplikacia.repository.SpravyRepository
    import com.example.skolskaplikacia.repository.ZnamkyRepository
    import com.example.skolskaplikacia.uiStates.BlokTextu
    import com.example.skolskaplikacia.uiStates.MenuUiState
    import com.example.skolskaplikacia.uiStates.Quadruple
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
        private val znamkyRepository: ZnamkyRepository,
        private val dochadzkaRepository: DochadzkaRepository
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(MenuUiState(meno = "", priezvisko = "", selectUser = 0, blokyVDni = listOf(), zoznamDeti = listOf()))
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
                if (osoba != null && (osoba.meno != _uiState.value.meno || osoba.priezvisko != _uiState.value.priezvisko)) {
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

        suspend fun LoadData(context: Context) {
            if (_uiState.value.reload) {
                resetUiState()
                _uiState.update { it.copy(reload = false) }
                val (existRozvrh, existSpravy) = DatabaseRequest()
                val deti = detiRepository.getAllDeti()
                if (deti.isNotEmpty()) {
                    _uiState.update { it.copy(selectUser = deti.first().dietaId, zoznamDeti = deti) }
                    setBlokovRozvrhu()
                    deti.forEach { item ->
                        val (rozvrh, spravy, znamky, dochadzka) = ServerRequest(item.dietaId, context)
                        if (rozvrh.isNotEmpty()) {
                            val (toAddRozrvrh, toDeleteRozvrh, toUpdateRozvrh) = PorovnajDatabazuRozvrh(rozvrh, existRozvrh, item.dietaId)
                            PridajDoRozvrhu(toAddRozrvrh)
                            OdstranZRozvrhu(toDeleteRozvrh)
                            UpravVRozvrhu(toUpdateRozvrh, item.dietaId)
                        }
                        if (spravy.isNotEmpty()) {
                            val (toAddSpravy, toDeleteSpravy) = PorovnajSpravy(spravy, existSpravy, item.dietaId)
                            PridajDoSpravy(toAddSpravy)
                            OdstranZSpravy(toDeleteSpravy)
                        }
                        if (dochadzka.isNotEmpty()) {
                            PridajDoDochadzky(dochadzka, item.dietaId)
                        }
                        if (znamky.isNotEmpty()) {
                            UpdateZnamkyDatabaza(znamky, item.dietaId)
                        }
                    }
                } else {
                    val osoba = osobaRepository.jePrihlaseny()
                    if (osoba != null) {
                        _uiState.update { it.copy(reload = false, selectUser = osoba.osobaId) }
                        setBlokovRozvrhu()
                        val (rozvrh, spravy, znamky, dochadzka) = ServerRequest(osoba.osobaId, context)
                        if (rozvrh.isNotEmpty()) {
                            val (toAddRozrvrh, toDeleteRozvrh, toUpdateRozvrh) = PorovnajDatabazuRozvrh(rozvrh, existRozvrh, osoba.osobaId)
                            PridajDoRozvrhu(toAddRozrvrh)
                            OdstranZRozvrhu(toDeleteRozvrh)
                            UpravVRozvrhu(toUpdateRozvrh, osoba.osobaId)
                        }
                        if (spravy.isNotEmpty()) {
                            val (toAddSpravy, toDeleteSpravy) = PorovnajSpravy(spravy, existSpravy, osoba.osobaId)
                            PridajDoSpravy(toAddSpravy)
                            OdstranZSpravy(toDeleteSpravy)
                        }
                        if (dochadzka.isNotEmpty()) {
                            PridajDoDochadzky(dochadzka, osoba.osobaId)
                        }
                        if (znamky.isNotEmpty()) {
                            UpdateZnamkyDatabaza(znamky, osoba.osobaId)
                        }
                    }
                }
                setBlokovRozvrhu()
            }
        }

        suspend fun ServerRequest(userId: Int, context: Context):Quadruple <List<RozvrhTuple>, List<SpravyTuple>, List<ZnamkyTuple>, List<dochadzkaDen>> {

            val dochadzkaDeferred = viewModelScope.async {
                try {
                    RetrofitClient.apiService.MobileGetDochadzka(UserId(userId)).list
                } catch (e: Exception) {
                    //println("Chyba pri načítaní dochádzky: ${e.localizedMessage}")
                    emptyList<dochadzkaDen>()
                }
            }
            val rozvrhDeferred = viewModelScope.async {
                try {
                    RetrofitClient.apiService.getUserRozvrh(UserId(userId)).list
                } catch (e: Exception) {
                    //println("Chyba pri načítaní rozvrhu: ${e.localizedMessage}")
                    emptyList<RozvrhTuple>()
                }
            }
            val spravyDeferred = viewModelScope.async {
                try {
                    RetrofitClient.apiService.MobileGetSpravy(UserId(userId)).list
                } catch (e: Exception) {
                    //println("Chyba pri načítaní správ: ${e.localizedMessage}")
                    emptyList<SpravyTuple>()
                }
            }
            val znamkyDeferred = viewModelScope.async {
                try {
                    RetrofitClient.apiService.MobileGetZnamky(UserId(userId)).list
                } catch (e: Exception) {
                    //println("Chyba pri načítaní známok: ${e.localizedMessage}")
                    emptyList<ZnamkyTuple>()
                }
            }
            val rozvrh = rozvrhDeferred.await()
            val spravy = spravyDeferred.await()
            val znamky = znamkyDeferred.await()
            val dochadzka = dochadzkaDeferred.await()
            if (rozvrh.isEmpty() or spravy.isEmpty() or znamky.isEmpty() or dochadzka.isEmpty()) Toast.makeText(context, "Chyba spojenia so serverom", Toast.LENGTH_SHORT).show()
            return Quadruple(rozvrh, spravy, znamky, dochadzka)
        }

        suspend fun DatabaseRequest():Pair <List<Rozvrh>, List<Spravy>> {
            val existujucaDatabazaRequest = viewModelScope.async { rozvrhRepository.getAllRozvrh() }
            val existujuceSpravyRequest = viewModelScope.async { spravyRepository.getAllSpravy() }
            val existujuciRozvrh = existujucaDatabazaRequest.await()
            val existujuceSpravy = existujuceSpravyRequest.await()
            return Pair(existujuciRozvrh, existujuceSpravy)
        }

        suspend fun UpdateZnamkyDatabaza(znamky: List<ZnamkyTuple> ,osobaID: Int) {
            znamkyRepository.deletePredmety(osobaID)
            znamky.forEach { predmet ->
                val predmetId = znamkyRepository.vlozitPredmet(Predmety(predmet = predmet.predmet, osobaId = osobaID))
                predmet.kategorie.forEach { kategoria ->
                    val kategoriaId = znamkyRepository.vlozitKategoriu(
                        Kategorie(predmetId = predmetId.toInt(), kategoriaId = kategoria.kategoria_id,
                            max_body = kategoria.max_body, nazov = kategoria.kategoriaNazov, vaha = kategoria.vaha, typ = kategoria.typ_znamky)
                    )
                    kategoria.znamkyPodpis.forEach { znamka ->
                        znamkyRepository.vlozitZnamka(Znamky(kategoriaId = kategoriaId.toInt(), znamka = znamka.znamka, podpis = 1))
                    }
                    kategoria.znamkyNePodpis.forEach { znamka ->
                        znamkyRepository.vlozitZnamka(Znamky(kategoriaId = kategoriaId.toInt(), znamka = znamka.znamka, podpis = 0))
                    }
                }
            }
            try {
                val znamkaZprepoctu = RetrofitClient.apiService.MobileGetRozsahZnamok(znamkyRepository.getAllKategoriaId()).list
                znamkaZprepoctu.forEach { znamka ->
                    znamkyRepository.VlozitNaPrepocet(ZnamkaNaPrepocet(
                        kategoriaId = znamka.kategoria_id,
                        znamka = znamka.znamka,
                        rozsah_do = znamka.znamka_do,
                        rozsah_od = znamka.znamka_od))
                }
            } catch (_: Exception) {

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

        suspend fun PridajDoDochadzky(zoznam: List<dochadzkaDen>, osobaID: Int) {
            dochadzkaRepository.deleteAllDochadzka(osobaID)
            for (den in zoznam) {
                for (blok in den.bloky) {
                    dochadzkaRepository.insertdochadzka(Dochadzka(
                        osobaId = osobaID,
                        typ = blok.typ,
                        den = den.datum,
                        block = blok.block,
                        ospravedlnene = blok.ospravedlnene,
                        id_dochadza = blok.id_dochadza,
                        poznamka = blok.poznamka
                    ))
                }
            }
        }
    }