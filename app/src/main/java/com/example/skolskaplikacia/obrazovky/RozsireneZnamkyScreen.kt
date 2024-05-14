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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.skolskaplikacia.Obrazovky
import com.example.skolskaplikacia.R
import com.example.skolskaplikacia.uiStates.Kategorie
import com.example.skolskaplikacia.viewModels.RozsireneZnamkyViewModel

@Composable
fun RozsireneZnamkyScreen(
    modifier: Modifier = Modifier,
    RZViewModel: RozsireneZnamkyViewModel,
    navController: NavController,
    predmetID: Int,
    rodic: Int
) {
    val uiStateRZ by RZViewModel.uiState.collectAsState()

    LaunchedEffect(predmetID) {
        if (predmetID != uiStateRZ.predmetId) RZViewModel.loadData(predmetID)
    }
    val configuration = LocalConfiguration.current
    val context = LocalContext.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    var prvaCast = 0.2F
    var druhaCast = 0.8F
    if (!isPortrait) {
        prvaCast = 0.3F
        druhaCast = 0.7F
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(prvaCast)
                .fillMaxWidth()
                .background(Color(0xFF8ECEC0))
        ) {
            if (isPortrait) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = { navController.popBackStack("${Obrazovky.známky.name}/${uiStateRZ.userId}/${rodic}", inclusive = false) },
                        modifier = Modifier
                            .align(Alignment.Start)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.back),
                            contentDescription = null,
                            Modifier.size(25.dp)
                        )
                    }
                    Text(
                        text = uiStateRZ.nazovPredmetu,
                        color = Color.Black,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Celkový priemer: ${uiStateRZ.priemer}",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (rodic == 1) {
                            Button(
                                onClick = { RZViewModel.PodpisanieZnamok(navController, rodic, context) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1F), contentColor = Color.Black)
                            ) {
                                Text(text = "Podpísať")
                            }
                        }
                    }
                }
            } else {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { navController.popBackStack("${Obrazovky.známky.name}/${uiStateRZ.userId}/${rodic}", inclusive = false) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.back),
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiStateRZ.nazovPredmetu,
                                color = Color.Black,
                                fontSize = 27.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(Modifier.size(40.dp))
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Celkový priemer: ${uiStateRZ.priemer}",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (rodic == 1) {
                            Button(
                                onClick = { RZViewModel.PodpisanieZnamok(navController, rodic, context) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1F), contentColor = Color.Black)
                            ) {
                                Text(text = "Podpísať")
                            }
                        }
                    }
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
            LazyColumn(modifier = modifier
                .fillMaxSize()
                .padding(8.dp)) {
                items(uiStateRZ.zoznamKategorii) { kategoria ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(Color.White)
                    ) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {
                            Column(modifier = Modifier.weight(0.7f)) {
                                Text(
                                    color = Color.Black,
                                    text = kategoria.nazov,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Váha: " + kategoria.vaha + " Typ: " + kategoria.typ,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                ZobrazZnamkyText(kategoria)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(0.15f)
                                    .align(Alignment.CenterVertically),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    color = priemerFarba(kategoria.priemer),
                                    text = kategoria.priemer,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}
@Composable
fun ZobrazZnamkyText(kategoria: Kategorie) {
    val znamkyText = buildAnnotatedString {
        kategoria.zoznamZnakom.forEachIndexed { index, znamka ->
            if (index > 0) append(" ")
            val znamkaColor = if (znamka.podpisane == 1) Color.Black else Color.Blue
            val znamkaStyle = SpanStyle(color = znamkaColor, fontSize = 22.sp)
            val prepocetStyle = SpanStyle(color = Color.Gray, fontSize = 15.sp)

            withStyle(znamkaStyle) {
                append(znamka.znamka.toString())
            }
            if (znamka.prepocet != null) {
                withStyle(prepocetStyle) {
                    append(" > " + znamka.prepocet)
                }
            }
        }
    }
    Text(text = znamkyText)
}
