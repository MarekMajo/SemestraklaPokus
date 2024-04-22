package com.example.skolskaplikacia.obrazovky

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.skolskaplikacia.viewModels.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
    LogoutBTN : () -> Unit = {}
) {
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