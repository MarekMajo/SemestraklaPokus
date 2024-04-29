package com.example.skolskaplikacia.obrazovky

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.skolskaplikacia.viewModels.RozvrhViewModel

@Composable
fun RozvrhScreen(
    modifier: Modifier = Modifier,
    rozvrhViewModel: RozvrhViewModel,
    BackButton: () -> Unit = {},
) {
    Button(onClick = { BackButton() }) {
        Text(text = "späť")
    }

}