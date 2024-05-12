import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skolskaplikacia.R
import com.example.skolskaplikacia.viewModels.DochadzkaViewModel

@Composable
fun DochadzkaScreen(
    modifier: Modifier = Modifier,
    dochadzkaViewModel: DochadzkaViewModel,
    BackButton: () -> Unit = {},
    userId: Int
) {
    val uiStateDochadzka by dochadzkaViewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    var prvaCast = 0.15F
    var druhaCast = 0.85F
    if (!isPortrait) {
        prvaCast = 0.4F
        druhaCast = 0.6F
    }
    LaunchedEffect(userId){
        if (userId != uiStateDochadzka.selectUser) {
            dochadzkaViewModel.loadData(userId)
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(prvaCast)
                .fillMaxWidth()
                .background(Color(0xFF8ECEC0))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = { BackButton() }
                    ) {
                        Image(
                            painter = painterResource(R.drawable.back), 
                            contentDescription = null,
                            Modifier.size(25.dp)
                        )
                    }
                    Text(
                        text = "Dochádzka",
                        color = Color.Black,
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxWidth(),
                        fontSize = 27.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Počet Vymeškaných hodín: ${uiStateDochadzka.celkoveChybanie}",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Počet Ospravedlnených hodín: ${uiStateDochadzka.pocetOspravedlnenych}",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Počet Neospravedlnenýc hodín: ${uiStateDochadzka.pocetNeospravedlnenych}",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
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
                items(uiStateDochadzka.dochadzka) { den ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(Color.White)
                    ) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)) {
                            Text(
                                color = Color.Black,
                                text = den.datum,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )

                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                for (i in 1..7) {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(2.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "$i", fontSize = 12.sp, color = Color.Black)
                                    }
                                }
                            }
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                (1..7).forEach { blockNumber ->
                                    val block = den.bloky.find { it.block == blockNumber }

                                    val image = when {
                                        block != null && block.typ == 1 && block.ospravedlnene == 0 -> R.drawable.nepritomny
                                        block != null && block.typ == 1 && block.ospravedlnene == 1 -> R.drawable.akcept
                                        block != null && block.typ == 1 && block.ospravedlnene == 2 -> R.drawable.deny
                                        block != null && block.typ == 2 && block.ospravedlnene == 0 -> R.drawable.meskanie
                                        block != null && block.typ == 2 && block.ospravedlnene == 1 -> R.drawable.meskanie_akcept
                                        block != null && block.typ == 2 && block.ospravedlnene == 2 -> R.drawable.meskanie_deny
                                        else -> R.drawable.pritomny
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                    ) {
                                        Image(
                                            painter = painterResource(id = image),
                                            contentDescription = null,
                                            modifier = Modifier.padding(4.dp)
                                        )
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
