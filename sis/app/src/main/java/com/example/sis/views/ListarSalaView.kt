package com.example.sis.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.sis.R
import com.example.sis.logic.logicRoom.RoomResult
import com.example.sis.ui.theme.SISTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarSalaView(
    navController: NavController,
    viewModel: RoomViewModel = viewModel()
) {
    val roomResult by viewModel.roomResult

    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val allSalas = listOf("Sala J", "Sala I", "Sala L", "Sala F", "Sala 218")
    val (filteredSalas, setFilteredSalas) = remember { mutableStateOf(allSalas) }
    LaunchedEffect(Unit) {
        viewModel.fetchRooms()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.login),
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
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.schedule),
                        contentDescription = "Schedule",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.account),
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.setting), // Icono extra
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

            // Mostrar las salas filtradas
            when (roomResult) {
                is RoomResult.Success -> {
                    val rooms = (roomResult as RoomResult.Success).rooms
                    rooms.forEach { room ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF2C663)
                            )
                        ) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    val img = rememberAsyncImagePainter(room.image)
                                    Image(
                                        painter = img,
                                        contentDescription = "Imagen de la sala",
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = room.name,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = room.description ?: "Sin descripción",
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        )
                                    }
                                }
                                Button(
                                    onClick = { /* Acción de reserva */ },
                                    modifier = Modifier
                                        .height(40.dp)
                                        .padding(start = 8.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0A5795),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text(text = "Reservar")
                                }

                            }
                        }
                    }
                }
                is RoomResult.Error -> {
                    Text(
                        text = (roomResult as RoomResult.Error).message,
                        color = Color.Red,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(16.dp)
                    )
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
