package com.example.skolskaplikacia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.skolskaplikacia.obrazovky.LoginScreen
import com.example.skolskaplikacia.obrazovky.MenuScreen

enum class Obrazovky() {
    login,
    menu,
    znamky
}

@Composable
fun Aplikacia(
    navController: NavHostController = rememberNavController(),
) {
     NavHost(
         navController = navController,
         startDestination = Obrazovky.login.name,
         modifier = Modifier
     ) {
         composable(route = Obrazovky.login.name) {
             LoginScreen(modifier = Modifier
                 .background(Color(0xFF8ECEC0))
                 .fillMaxSize(),
                 LoginBTN = { navController.navigate(Obrazovky.menu.name)}

             )
         }

         composable(route = Obrazovky.menu.name) {
             MenuScreen(modifier = Modifier
                 .background(Color(0xFF8ECEC0))
                 .fillMaxSize(),
                 LogoutBTN = { navController.navigate(Obrazovky.login.name) }
             )
         }
     }
}


