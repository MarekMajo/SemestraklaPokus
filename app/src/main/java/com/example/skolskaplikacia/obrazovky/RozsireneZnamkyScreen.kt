package com.example.skolskaplikacia.obrazovky

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.skolskaplikacia.uiStates.VysledokPredmetu
import com.example.skolskaplikacia.viewModels.RozsireneZnamkyViewModel

@Composable
fun RozsireneZnamkyScreen(
    modifier: Modifier = Modifier,
    RZViewModel: RozsireneZnamkyViewModel,
    navController: NavController,
    predmetID: Int
) {
    val uiStateRZ by RZViewModel.uiState.collectAsState()

    LaunchedEffect(predmetID) {
        if (predmetID != uiStateRZ.predmetId) RZViewModel.loadData(predmetID)
    }
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
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
                    onClick = {navController.popBackStack(Obrazovky.známky.name, inclusive = false)}
                ) {
                    Image(painter = painterResource(R.drawable.back), contentDescription = null)
                }
                Text(
                    text = uiStateRZ.nazovPredmetu,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(),
                    fontSize = 27.sp,
                    textAlign = TextAlign.Center
                )
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
            LazyColumn(modifier = modifier.fillMaxSize().padding(8.dp)) {
                items(uiStateRZ.zoznamKategorii) { kategoria ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(Color.White)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                            Column(modifier = Modifier.weight(0.7f)) {
                                Text(
                                    color = Color.Black,
                                    text = kategoria.nazov,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold
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
            val style = SpanStyle(color = znamkaColor, fontSize = 20.sp)
            withStyle(style) {
                append(znamka.znamka.toString())
            }
        }
    }
    Text(text = znamkyText)
}