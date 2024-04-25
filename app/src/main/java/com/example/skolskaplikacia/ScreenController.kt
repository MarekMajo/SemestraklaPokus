package com.example.skolskaplikacia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.skolskaplikacia.obrazovky.LoginScreen
import com.example.skolskaplikacia.obrazovky.MenuScreen
import com.example.skolskaplikacia.repository.DatabaseFactory
import com.example.skolskaplikacia.repository.OsobaRepository
import com.example.skolskaplikacia.uiStates.blokyTextov
import com.example.skolskaplikacia.viewModels.LoginViewModel
import com.example.skolskaplikacia.viewModels.MenuViewModel

enum class Obrazovky() {
    login,
    menu,
    znamky
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
    val loginViewModel: LoginViewModel = viewModel(factory = DatabaseFactory(osobaRepository))
    val menuViewModel: MenuViewModel = viewModel(factory = DatabaseFactory(osobaRepository))
    val uiState by loginViewModel.uiState.collectAsState()

    // Sleduje zmeny userID a riadi navigáciu na základe týchto zmien.
    LaunchedEffect(uiState.userID) {
        if (uiState.userID > 0) {
            navController.navigate(Obrazovky.menu.name) {
                popUpTo(Obrazovky.login.name) { inclusive = true } // Odstráni prihlasovaciu obrazovku zo stacku
            }
        } else {
            navController.navigate(Obrazovky.login.name)
        }
    }

    // Nastavuje počiatočnú obrazovku založenú na stavu userID
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
                viewModel = loginViewModel,
                LoginBTN = { navController.navigate(Obrazovky.menu.name)}
            )
        }
        composable(route = Obrazovky.menu.name) {
            MenuScreen(
                modifier = Modifier,
                loginViewModel = loginViewModel,
                menuViewModel = menuViewModel
                //LogoutBTN = { navController.navigate(Obrazovky.login.name) }
            )
        }
    }
}
