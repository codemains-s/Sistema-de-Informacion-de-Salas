package com.example.sis.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sis.R
import com.example.sis.ui.theme.SISTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarSalaView() {

    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val allSalas = listOf("Sala J", "Sala I", "Sala L", "Sala F", "Sala 218")
    val (filteredSalas, setFilteredSalas) = remember { mutableStateOf(allSalas) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.register),
                            contentDescription = "Logo Universidad",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Universidad de Caldas",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "SIS",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF0A5795)
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF0A5795)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Home",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Schedule",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ucaldas_fondo2),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ucaldas_fondo1),
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background), // Icono extra
                        contentDescription = "Settings",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color.White),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Buscador
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xffffffff)
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = { newText ->
                            setSearchText(newText)
                            filterSalas(newText, allSalas, setFilteredSalas)
                        },
                        placeholder = { Text(text = "ej: sala j") },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color(0xff888888),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { /* Acción del botón de buscar */ },
                        modifier = Modifier.height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0A5795),
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Buscar")
                    }
                }
            }

            // Lista de resultados
            filteredSalas.forEach { sala ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF2C663)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.register),
                            contentDescription = "Imagen de la sala",
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = sala,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Información de la sala...",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Otra línea de información..",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Otra línea de información..",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }

}

private fun filterSalas(
    searchText: String,
    allSalas: List<String>,
    setFilteredSalas: (List<String>) -> Unit
) {
    val filteredSalas = if (searchText.isEmpty()) {
        allSalas
    } else {
        allSalas.filter { sala ->
            sala.lowercase().contains(searchText.lowercase())
        }
    }
    setFilteredSalas(filteredSalas)
}


@Preview(showBackground = true)
@Composable
fun Screen() {
    SISTheme {
        ListarSalaView()
    }
}