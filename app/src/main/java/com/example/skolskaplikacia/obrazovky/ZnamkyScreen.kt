package com.example.skolskaplikacia.obrazovky

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.skolskaplikacia.Obrazovky
import com.example.skolskaplikacia.viewModels.ZnamkyViewModel

@Composable
fun ZnamkyScreen(
    modifier: Modifier = Modifier,
    znamkyViewModel: ZnamkyViewModel,
    navController: NavController,
) {
    Column {
        Button(onClick = { navController.popBackStack(Obrazovky.menu.name, inclusive = false) }) {
            Text(text = "späť")
        }
        Button(onClick = { navController.navigate(Obrazovky.rozsirene.name) }) {
            Text(text = "Rozšírené")
        }
    }
}