package com.example.skolskaplikacia.obrazovky

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.skolskaplikacia.Obrazovky
import com.example.skolskaplikacia.R
import com.example.skolskaplikacia.databaza.Deti
import com.example.skolskaplikacia.uiStates.BlokCasu
import com.example.skolskaplikacia.uiStates.BlokTextu
import com.example.skolskaplikacia.uiStates.MenuUiState
import com.example.skolskaplikacia.uiStates.blokyCasov
import com.example.skolskaplikacia.viewModels.LoginViewModel
import com.example.skolskaplikacia.viewModels.MenuViewModel
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext


@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginViewModel,
    menuViewModel: MenuViewModel
) {
    val uiStatelogin by loginViewModel.uiState.collectAsState()
    val uiStatemenu by menuViewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(uiStatelogin.userID) {
        if (uiStatelogin.userID > 0) {
            menuViewModel.setUsername()
            menuViewModel.LoadData(context)
        } else if (uiStatelogin.userID == 0) {
            menuViewModel.LoadData(context)
            menuViewModel.PovolReaload(true)
        }
    }

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val gradientColors = listOf(Color(0xFF8ECEC0), Color(0xFFF1F1F1))

    Column(modifier = modifier.fillMaxSize().background(
        Brush.verticalGradient(
            colors = gradientColors,
            startY = 0f,
            endY = Float.POSITIVE_INFINITY
        )
    )) {
        Box(
            modifier = Modifier
                .weight(if (isPortrait) 0.2F else 0.8F)
                .fillMaxWidth()
        ) {
            Column {
                Row {
                    UzivatelButton(
                        meno = uiStatemenu.meno ?: "",
                        priezvisko = uiStatemenu.priezvisko ?: "",
                        loginViewModel = loginViewModel
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    DetiButton(zoznam = uiStatemenu.zoznamDeti, menuViewModel, uiStatemenu)
                }
                Spacer(Modifier.height(if (isPortrait) 50.dp else 0.dp))
                MenuRozvrh(blokyCasov, uiStatemenu.blokyVDni)
            }
        }
        Column(
            modifier = Modifier
                .weight(if (isPortrait) 0.3F else 0.7F)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val rodic = if (uiStatemenu.zoznamDeti.size > 0) 1 else 0
            if (isPortrait) {
                Row (modifier = Modifier.padding(bottom = 4.dp, top = 20.dp)) {
                    NavigacneTlacidla("Správy", R.drawable.spravy, navController, uiStatemenu.selectUser, rodic)
                    Spacer(Modifier.width(8.dp))
                    NavigacneTlacidla("Dochádzka", R.drawable.dochadzka, navController, uiStatemenu.selectUser, rodic)
                }
                Row (modifier = Modifier.padding(top = 4.dp)) {
                    NavigacneTlacidla("Známky", R.drawable.znamky, navController, uiStatemenu.selectUser, rodic)
                    Spacer(Modifier.width(8.dp))
                    NavigacneTlacidla("Rozvrh", R.drawable.rozvrh, navController, uiStatemenu.selectUser, rodic)
                }
            } else {
                Row (modifier = Modifier.padding(top = 30.dp))  {
                    NavigacneTlacidla("Správy", R.drawable.spravy, navController, uiStatemenu.selectUser, rodic)
                    Spacer(Modifier.width(8.dp))
                    NavigacneTlacidla("Dochádzka", R.drawable.dochadzka, navController, uiStatemenu.selectUser, rodic)
                    Spacer(Modifier.width(8.dp))
                    NavigacneTlacidla("Známky", R.drawable.znamky, navController, uiStatemenu.selectUser, rodic)
                    Spacer(Modifier.width(8.dp))
                    NavigacneTlacidla("Rozvrh", R.drawable.rozvrh, navController, uiStatemenu.selectUser, rodic)
                }
            }
        }
    }
}
@Composable
fun UzivatelButton(meno: String, priezvisko: String, loginViewModel: LoginViewModel) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .padding(15.dp)
        .background(Color.White.copy(alpha = 0.01F))
        .padding(5.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            text = "$meno $priezvisko",
            modifier = Modifier.clickable { expanded = true },
            color = Color.Black
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(text = { Text(text = "Odhlasiť") }, onClick = {
                expanded = false
                loginViewModel.logout()
            })
        }
    }
}

@Composable
fun DetiButton(zoznam: List<Deti>, menuViewModel: MenuViewModel , uiStatemenu: MenuUiState) {
    var expanded by remember { mutableStateOf(false) }
    if (zoznam.isNotEmpty()) {
        Box(
            modifier = Modifier
                .padding(15.dp)
                .background(Color.White.copy(alpha = 0.01F))
                .padding(5.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            for (item in zoznam) {
                if (item.dietaId == uiStatemenu.selectUser) {
                    Text(
                        text = "${item.meno} ${item.priezvisko}",
                        modifier = Modifier.clickable { expanded = true },
                        color = Color.Black
                    )
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                for (item in zoznam) {
                    if (item.dietaId != uiStatemenu.selectUser)
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${item.meno} ${item.priezvisko}"
                                )
                            },
                            onClick = {
                                expanded = false
                                menuViewModel.ChangeSelectUser(item.dietaId)
                            })
                }
            }
        }
    }
}

@Composable
fun NavigacneTlacidla(text: String, image: Int, navController: NavController, selectUser: Int, rodic: Int) {
    Button(
        modifier = Modifier
            .size(width = 200.dp, height = 100.dp)
            .padding(1.dp),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.1F), contentColor = Color.Black),
        onClick = {
            navController.navigate("${Obrazovky.valueOf(text.toLowerCase()).name}/${selectUser}/${rodic}")
        }
    ) {
        Image(painter = painterResource(id = image), contentDescription = null, modifier = Modifier.width(50.dp))
        Spacer(Modifier.width(5.dp))
        Text(text = text)
    }
}



@Composable
fun MenuRozvrh(blokyCasov: List<BlokCasu>, blokyTextov: List<BlokTextu>) {
    val velkost = LocalConfiguration.current.screenWidthDp.dp / 7
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)
    ) {
        for (i in 1..7) {
            val blok = blokyCasov.find { it.id == i } ?: BlokCasu(i, listOf("", ""))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(height = 50.dp, width = velkost)
                    .border(BorderStroke(1.dp, Color.Black))
            ) {
                Column {
                    blok.cas.forEach { text ->
                        Text(text = text, color = Color.Black)
                    }
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)
    ) {
        for (i in 1..7) {
            val blok = blokyTextov.find { it.id == i } ?: BlokTextu(i, listOf("", ""))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(velkost)
                    .border(BorderStroke(1.dp, Color.Black))
                    .padding(1.dp)
            ) {
                Column {
                    blok.texty.forEach { text ->
                        Text(text = text, color = Color.Black)
                    }
                }
            }
        }
    }
}