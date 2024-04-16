package com.example.skolskaplikacia.obrazovky

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    LogoutBTN : () -> Unit = {}
) {
    Column {
        Text(text = "Menu Sceen")
        Button(
            onClick = LogoutBTN
        ) {
           Text(text = "Odhlasiť") 
        }
    }
}