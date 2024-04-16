package com.example.skolskaplikacia

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

enum class Obrazovky() {
    login,
    menu,
    znamky
}

@Composable
fun Aplikacia(
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {

}