package com.example.skolskaplikacia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skolskaplikacia.databaza.AppDatabaza
import com.example.skolskaplikacia.obrazovky.DochadzkaScreen
import com.example.skolskaplikacia.obrazovky.LoginScreen
import com.example.skolskaplikacia.obrazovky.MenuScreen
import com.example.skolskaplikacia.obrazovky.RozsireneZnamkyScreen
import com.example.skolskaplikacia.obrazovky.RozvrhScreen
import com.example.skolskaplikacia.obrazovky.SpravyScreen
import com.example.skolskaplikacia.obrazovky.ZnamkyScreen
import com.example.skolskaplikacia.repository.DatabaseFactory
import com.example.skolskaplikacia.repository.DetiRepository
import com.example.skolskaplikacia.repository.OsobaRepository
import com.example.skolskaplikacia.repository.RozvrhRepository
import com.example.skolskaplikacia.repository.SpravyRepository
import com.example.skolskaplikacia.repository.ZnamkyRepository
import com.example.skolskaplikacia.viewModels.DochadzkaViewModel
import com.example.skolskaplikacia.viewModels.LoginViewModel
import com.example.skolskaplikacia.viewModels.MenuViewModel
import com.example.skolskaplikacia.viewModels.RozsireneZnamkyViewModel
import com.example.skolskaplikacia.viewModels.RozvrhViewModel
import com.example.skolskaplikacia.viewModels.SpravyViewModel
import com.example.skolskaplikacia.viewModels.ZnamkyViewModel

enum class Obrazovky {
    login,
    menu,
    správy,
    dochádzka,
    známky,
    rozsirene,
    rozvrh
}


/**
 * Hlavná composable funkcia, ktorá riadi navigáciu a zobrazenie hlavných obrazoviek aplikácie.
 * Umožňuje dynamickú navigáciu medzi obrazovkami založené podľa stavu používateľa.
 *
 * @param navController Kontrolér pre navigáciu, ktorý umožňuje prechody medzi obrazovkami.
 */
@Composable
fun Aplikacia(
    navController: NavHostController = rememberNavController(),
) {
    val appContext = LocalContext.current.applicationContext
    val db = AppDatabaza.getDatabase(appContext)
    val osobaRepository = OsobaRepository(db.osobaDao())
    val rozvrhRepository = RozvrhRepository(db.rozvrhDao())
    val detiRepository = DetiRepository(db.detiDao())
    val spravyRepository = SpravyRepository(db.spravyDao())
    val znamkyRepository = ZnamkyRepository(db.znamkyDao())
    val loginViewModel: LoginViewModel = viewModel(factory = DatabaseFactory(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository))
    val menuViewModel: MenuViewModel = viewModel(factory = DatabaseFactory(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository))
    val spravyViewModel: SpravyViewModel = viewModel(factory = DatabaseFactory(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository))
    val rozvrhViewModel: RozvrhViewModel = viewModel(factory = DatabaseFactory(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository))
    val dochadzkaViewModel: DochadzkaViewModel = viewModel()
    val znamkyViewModel: ZnamkyViewModel = viewModel(factory = DatabaseFactory(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository))
    val rozsireneZnamkyViewModel: RozsireneZnamkyViewModel = viewModel()
    val uiState by loginViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (uiState.userID > 0) Obrazovky.menu.name else Obrazovky.login.name,
        modifier = Modifier
    ) {
        composable(route = Obrazovky.login.name) {
            LoginScreen(
                modifier = Modifier
                    .background(Color(0xFF8ECEC0))
                    .fillMaxSize(),
                viewModel = loginViewModel
            )
        }
        composable(route = Obrazovky.menu.name) {
            MenuScreen(
                modifier = Modifier,
                navController = navController,
                loginViewModel = loginViewModel,
                menuViewModel = menuViewModel
            )
        }
        composable(route = Obrazovky.správy.name) {
            SpravyScreen(
                modifier = Modifier,
                menuViewModel = menuViewModel,
                spravyViewModel = spravyViewModel,
                BackButton = {navController.popBackStack(Obrazovky.menu.name, inclusive = false)}
            )
        }
        composable(route = Obrazovky.rozvrh.name) {
            RozvrhScreen(
                modifier = Modifier,
                menuViewModel = menuViewModel,
                rozvrhViewModel = rozvrhViewModel,
                BackButton = {navController.popBackStack(Obrazovky.menu.name, inclusive = false)}
            )
        }
        composable(route = Obrazovky.dochádzka.name) {
            DochadzkaScreen(
                modifier = Modifier,
                dochadzkaViewModel = dochadzkaViewModel,
                BackButton = {navController.popBackStack(Obrazovky.menu.name, inclusive = false)}
            )
        }
        composable(route = Obrazovky.známky.name) {
            ZnamkyScreen(
                modifier = Modifier,
                znamkyViewModel = znamkyViewModel,
                navController = navController
            )
        }
        composable(route = Obrazovky.rozsirene.name) {
            RozsireneZnamkyScreen(
                modifier = Modifier,
                rozsireneZnamkyViewModel = rozsireneZnamkyViewModel,
                navController = navController
            )
        }
    }
}
