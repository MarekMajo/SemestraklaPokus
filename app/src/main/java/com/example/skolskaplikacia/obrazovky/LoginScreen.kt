package com.example.skolskaplikacia.obrazovky

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.skolskaplikacia.R

@Composable
fun LoginScreen (
    modifier : Modifier = Modifier,
    LoginBTN: () -> Unit = {}
) {
    val prMeno = remember { mutableStateOf("") }
    val prHeslo = remember { mutableStateOf("") }

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
            onClick = LoginBTN
        ) {
            Text(text = "Prihlásiť")
        }
    }
}