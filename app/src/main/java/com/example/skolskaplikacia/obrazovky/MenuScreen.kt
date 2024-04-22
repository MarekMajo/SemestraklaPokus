package com.example.skolskaplikacia.obrazovky

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.skolskaplikacia.viewModels.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skolskaplikacia.databaza.AppDatabaza
import com.example.skolskaplikacia.repository.DatabaseFactory
import com.example.skolskaplikacia.repository.OsobaRepository

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    LogoutBTN : () -> Unit = {},
    viewModel: LoginViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    Column {
        Text(text = "Menu Screen")
        Button(
            onClick = {
                viewModel.logout()
                LogoutBTN()
            }
        ) {
           Text(text = "Odhlasiť") 
        }
    }
}