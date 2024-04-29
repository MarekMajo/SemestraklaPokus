package com.example.skolskaplikacia.obrazovky

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.skolskaplikacia.Obrazovky
import com.example.skolskaplikacia.viewModels.RozsireneZnamkyViewModel

@Composable
fun RozsireneZnamkyScreen(
    modifier: Modifier = Modifier,
    rozsireneZnamkyViewModel: RozsireneZnamkyViewModel,
    navController: NavController,
) {
    Button(onClick = { navController.popBackStack(Obrazovky.známky.name, inclusive = false) }) {
        Text(text = "späť")
    }

}