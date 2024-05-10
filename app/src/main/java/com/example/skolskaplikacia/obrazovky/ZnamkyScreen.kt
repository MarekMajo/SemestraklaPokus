package com.example.skolskaplikacia.obrazovky

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skolskaplikacia.Obrazovky
import com.example.skolskaplikacia.R
import com.example.skolskaplikacia.uiStates.blokyCasov
import com.example.skolskaplikacia.uiStates.blokyDni
import com.example.skolskaplikacia.viewModels.ZnamkyViewModel

@Composable
fun ZnamkyScreen(
    modifier: Modifier = Modifier,
    znamkyViewModel: ZnamkyViewModel,
    navController: NavController,
) {
    val uiStateznamky by znamkyViewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    var prvaCast = 0.05F
    var druhaCast = 0.95F
    if (!isPortrait) {
        prvaCast = 0.2F
        druhaCast = 0.8F
    }
    znamkyViewModel.vytvorZnamkyVysledok()

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(prvaCast)
                .fillMaxWidth()
                .background(Color(0xFF8ECEC0))

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = {navController.popBackStack(Obrazovky.menu.name, inclusive = false)}
                ) {
                    Image(painter = painterResource(R.drawable.back), contentDescription = null)
                }
                Text(
                    text = "Známky",
                    color = Color.Black,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(),
                    fontSize = 27.sp,
                    textAlign = TextAlign.Center
                )
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    onClick = {}
                ) {
                    Text(text = "Podpísať")
                }
            }

        }
        Column(
            modifier = Modifier
                .weight(druhaCast)
                .fillMaxWidth()
                .background(Color(0xfff1f1f1)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}