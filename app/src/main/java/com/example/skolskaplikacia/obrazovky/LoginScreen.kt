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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.skolskaplikacia.viewModels.LoginViewModel
import com.example.skolskaplikacia.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skolskaplikacia.databaza.AppDatabaza
import com.example.skolskaplikacia.network.RetrofitClient
import com.example.skolskaplikacia.repository.DatabaseFactory
import com.example.skolskaplikacia.repository.OsobaRepository

@Composable
fun LoginScreen (
    modifier : Modifier = Modifier,
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

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
            value = uiState.userName,
            onValueChange = {newText -> viewModel.updateUserName(newText)},
            label = { Text("Prihlasovacie Meno alebo ID")},
            singleLine = true
        )
        TextField(
            value = uiState.userPassword,
            onValueChange = {newText -> viewModel.updateUserPassword(newText)},
            label = { Text("Prihlasovacie Heslo")},
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Button(
            onClick = {
                viewModel.login(uiState.userName, uiState.userPassword)
            }
        ) {
            Text(text = "Prihlásiť")
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (uiState.userID) {
            -2 -> Text(text = "Nepodarilo sa pripojiť k sieti", color = Color.Red)
            -1 -> Text(text = "Nesprávne zadané prihlasovacie údaje", color = Color.Red)
        }
    }
}