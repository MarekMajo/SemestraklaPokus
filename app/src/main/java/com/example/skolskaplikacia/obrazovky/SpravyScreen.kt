package com.example.skolskaplikacia.obrazovky

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.skolskaplikacia.viewModels.SpravyViewModel

@Composable
fun SpravyScreen(
    modifier: Modifier = Modifier,
    spravyViewModel: SpravyViewModel,
    BackButton: () -> Unit = {},
) {
    Button(onClick = { BackButton() }) {
        Text(text = "späť")
    }

}