package com.example.skolskaplikacia.obrazovky

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skolskaplikacia.R
import com.example.skolskaplikacia.databaza.AppDatabaza
import com.example.skolskaplikacia.repository.DatabaseFactory
import com.example.skolskaplikacia.repository.OsobaRepository
import com.example.skolskaplikacia.repository.RozvrhRepository
import com.example.skolskaplikacia.uiStates.BlokCasu
import com.example.skolskaplikacia.uiStates.BlokTextu
import com.example.skolskaplikacia.uiStates.blokyCasov
import com.example.skolskaplikacia.uiStates.blokyTextov
import com.example.skolskaplikacia.viewModels.LoginViewModel
import com.example.skolskaplikacia.viewModels.MenuViewModel

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    menuViewModel: MenuViewModel
) {
    val uiStatelogin by loginViewModel.uiState.collectAsState()
    val uiStatemenu by menuViewModel.uiState.collectAsState()
    menuViewModel.setUsername()
    val blokyTextov = blokyTextov
    val blokyCasu = blokyCasov

    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    var prvaCast = 0.4F
    var druhaCast = 0.6F
    if (!isPortrait) {
        prvaCast = 0.5F
        druhaCast = 0.5F
    }
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(prvaCast)
                .fillMaxWidth()
                .background(Color(0xFF8ECEC0))
        ) {
            Column {
                UserNameButton(
                    meno = uiStatemenu.meno ?: "",
                    priezvisko = uiStatemenu.priezvisko ?: "",
                    //meno = "Marek",
                    //priezvisko = "Tvrdosinský",
                    loginViewModel = loginViewModel
                )
                GridOfSquares(blokyCasu, blokyTextov)
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
            if (isPortrait) {
                Row {
                    ActionButton("Správy", R.drawable.spravy)
                    Spacer(Modifier.width(8.dp))
                    ActionButton("Dochádzka", R.drawable.dochadzka)
                }
                Row {
                    ActionButton("Známky", R.drawable.znamky)
                    Spacer(Modifier.width(8.dp))
                    ActionButton("Rozvrh", R.drawable.rozvrh)
                }
            } else {
                Row {
                    ActionButton("Správy", R.drawable.spravy)
                    Spacer(Modifier.width(8.dp))
                    ActionButton("Dochádzka", R.drawable.dochadzka)
                    ActionButton("Známky", R.drawable.znamky)
                    Spacer(Modifier.width(8.dp))
                    ActionButton("Rozvrh", R.drawable.rozvrh)
                }
            }
        }
    }
}
@Composable
fun UserNameButton(meno: String, priezvisko: String, loginViewModel: LoginViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .padding(15.dp)
        .background(Color.White)
        .border(BorderStroke(1.dp, Color.Black))
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
            DropdownMenuItem(text = { Text(text = "Odhlasiť") }, onClick = { loginViewModel.logout() })
        }
    }
}

@Composable
fun ActionButton(text: String, image: Int ) {
    Button(
        modifier = Modifier
            .size(width = 200.dp, height = 200.dp)
            .padding(5.dp)
        ,
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
        onClick = { println("Stlačené tlačidlo" + text) }) {
        Image(painter = painterResource(
            id = image),
            contentDescription = null,
            modifier = Modifier
                .width(50.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(text = text)
    }
}

@Composable
fun GridOfSquares(blokyCasov: List<BlokCasu>, blokyTextov: List<BlokTextu>) {
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

@Preview
@Composable
fun Zobraz() {
    val appContext = LocalContext.current.applicationContext
    val db = AppDatabaza.getDatabase(appContext)
    val osobaRepository = OsobaRepository(db.osobaDao())
    val rozvrhRepository = RozvrhRepository(db.rozvrhDao())
    val loginViewModel: LoginViewModel = viewModel(factory = DatabaseFactory(osobaRepository,rozvrhRepository))
    val menuViewModel: MenuViewModel = viewModel(factory = DatabaseFactory(osobaRepository,rozvrhRepository))
    MenuScreen(
        modifier = Modifier,
        loginViewModel = loginViewModel,
        menuViewModel = menuViewModel
    )

}