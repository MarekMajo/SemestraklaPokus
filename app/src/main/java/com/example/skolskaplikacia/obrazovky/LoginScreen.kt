package com.example.skolskaplikacia.obrazovky

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.skolskaplikacia.AppViewModel
import com.example.skolskaplikacia.R

@Composable
fun LoginScreen (
    modifier : Modifier = Modifier,
    viewModel: AppViewModel,
    LoginBTN: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val prMeno = rememberSaveable { mutableStateOf("") }
    val prHeslo = rememberSaveable { mutableStateOf("") }


    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.width(200.dp)
        )
        TextField(
            value = prMeno.value,
            onValueChange = {newText -> prMeno.value = newText},
            label = { Text("Prihlasovacie Meno alebo ID")},
            singleLine = true
        )
        TextField(
            value = prHeslo.value,
            onValueChange = {newText -> prHeslo.value = newText},
            label = { Text("Prihlasovacie Heslo")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Button(
            onClick = {
                viewModel.login(prMeno.value, prHeslo.value)
            }
        ) {
            Text(text = "Prihlásiť")
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (uiState.userID) {
            -2 -> Text(text = "Nepodarilo sa pripojiť k sieti", color = Color.Red)
            -1 -> Text(text = "nesprávne prihlasovacie údaje", color = Color.Red)
            0 -> {}
            else -> LoginBTN()
        }


    }
}