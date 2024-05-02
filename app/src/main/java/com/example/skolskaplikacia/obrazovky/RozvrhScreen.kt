package com.example.skolskaplikacia.obrazovky

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skolskaplikacia.R
import com.example.skolskaplikacia.uiStates.BlokCasu
import com.example.skolskaplikacia.uiStates.BlokDna
import com.example.skolskaplikacia.uiStates.BlokRozvrhu
import com.example.skolskaplikacia.uiStates.BlokTextu
import com.example.skolskaplikacia.uiStates.blokyCasov
import com.example.skolskaplikacia.uiStates.blokyDni
import com.example.skolskaplikacia.viewModels.MenuViewModel
import com.example.skolskaplikacia.viewModels.RozvrhViewModel

@Composable
fun RozvrhScreen(
    modifier: Modifier = Modifier,
    menuViewModel: MenuViewModel,
    rozvrhViewModel: RozvrhViewModel,
    BackButton: () -> Unit = {},
) {
    val uiStatemenu by menuViewModel.uiState.collectAsState()
    val uiStaterozvrh by rozvrhViewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    LaunchedEffect(uiStatemenu.selectUser){
        if (uiStatemenu.selectUser != uiStaterozvrh.selectUser) {
            rozvrhViewModel.loadData(uiStatemenu.selectUser)
        }
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
                    text = "Rozvrh",
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
        Column(
            modifier = Modifier
                .weight(druhaCast)
                .fillMaxWidth()
                .background(Color(0xfff1f1f1)),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Rozvrh(blokyCasov, uiStaterozvrh.rozvrhZoznam, blokyDni)
        }
    }
}

@Composable
fun Rozvrh(blokyCasov: List<BlokCasu>, BlokRozvrhu: List<BlokRozvrhu>, blokyDni: List<BlokDna>) {
    val velkost = LocalConfiguration.current.screenWidthDp.dp / 6

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
                .background(Color.White)
        ) {
            for (i in 0..5) {
                val blok = blokyDni.find { it.id == i } ?: BlokDna(i, "")
                val tempVelkost = if (i == 0) {
                    velkost - 20.dp
                } else {
                    velkost + 6.dp
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(height = 50.dp, width = tempVelkost)
                        .border(BorderStroke(1.dp, Color.Black))
                ) {
                    Text(text = blok.den, color = Color.Black)
                }
            }
        }

        LazyColumn {
            items(7) { cas ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp)
                        .background(Color.White)
                ) {
                    val blokCasu = blokyCasov.find { it.id == cas + 1 } ?: BlokCasu(cas + 1, listOf("", ""))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(width = velkost - 20.dp, height = velkost)
                            .border(BorderStroke(1.dp, Color.Black))
                            .padding(1.dp)
                    ) {
                        Column {
                            blokCasu.cas.forEach { text ->
                                Text(text = text, color = Color.Black)
                            }
                        }
                    }
                    Row {
                        for (den in 1..5) {
                            val blok_ = BlokRozvrhu.find { it.blok == cas + 1 && it.den == den } ?: BlokRozvrhu(
                                den,
                                cas + 1,
                                listOf("", "")
                            )
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(width = velkost + 6.dp, height = velkost)
                                    .border(BorderStroke(1.dp, Color.Black))
                                    .padding(1.dp)
                                    .background(Color.White)
                            ) {
                                Column {
                                    blok_.texty.forEach { text ->
                                        Text(text = text, color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

