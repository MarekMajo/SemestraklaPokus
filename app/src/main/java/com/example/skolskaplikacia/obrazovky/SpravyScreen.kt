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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skolskaplikacia.R
import com.example.skolskaplikacia.viewModels.MenuViewModel
import com.example.skolskaplikacia.viewModels.SpravyViewModel

@Composable
fun SpravyScreen(
    modifier: Modifier = Modifier,
    spravyViewModel: SpravyViewModel,
    BackButton: () -> Unit = {},
    userId: Int
) {
    val uiStatespravy by spravyViewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    LaunchedEffect(userId) {
        if (userId != uiStatespravy.selectUser) spravyViewModel.loadData(userId)
    }
    var prvaCast = 0.05F
    var druhaCast = 0.95F
    if (!isPortrait) {
        prvaCast = 0.2F
        druhaCast = 0.8F
    }

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
                    onClick = BackButton
                ) {
                    Image(painter = painterResource(R.drawable.back), contentDescription = null)
                }
                Text(
                    text = "Oznámenia",
                    color = Color.Black,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(),
                    fontSize = 27.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.weight(1f))
            }

        }
        LazyColumn(
            modifier = Modifier
                .weight(druhaCast)
                .fillMaxWidth()
                .background(Color(0xfff1f1f1)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(uiStatespravy.spravyZoznam) { sprava ->
                SpravaComponent(sprava.datum, sprava.cas, sprava.sprava)
            }
        }
    }
}

@Composable
fun SpravaComponent(datum: String, cas: String, sprava: String?) {
    val velkost = LocalConfiguration.current.screenWidthDp.dp - 10.dp
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(color = Color.White)
            .size(width = velkost, height = 80.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = datum ,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = cas ,
                    color = Color.Black,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = sprava ?: "",
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth(),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
