package com.example.sis.views

import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
import androidx.navigation.NavController
import com.example.sis.R
import com.example.sis.conexion_api.ApiService
import com.example.sis.datamodels.Room
=======
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.sis.R
import com.example.sis.logic.logicRoom.RoomResult
>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
import com.example.sis.ui.theme.SISTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarSalaView(
    navController: NavController,
<<<<<<< HEAD
    modifier: Modifier = Modifier
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") } // Texto del buscador
    var allSalas by remember { mutableStateOf<List<Room>>(emptyList()) } // Lista de salas
    var filteredSalas by remember { mutableStateOf<List<Room>>(emptyList()) } // Lista filtrada
    var isLoading by remember { mutableStateOf(true) } // Estado de carga
    var errorMessage by remember { mutableStateOf<String?>(null) } // Mensaje de error

    val coroutineScope = rememberCoroutineScope()

    // Obtener las salas desde la API
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                errorMessage = null
                Log.d("API_REQUEST", "Enviando solicitud a ${ApiService.roomApi.allRoom()}")
                allSalas = ApiService.roomApi.allRoom() // Llamada a la API


                filteredSalas = allSalas // Inicializar con todos los datos
            } catch (e: Exception) {
                errorMessage = "Error al cargar las salas: ${e.message}"
            } finally {
                isLoading = false
            }
        }
=======
    viewModel: RoomViewModel = viewModel()
) {
    val roomResult by viewModel.roomResult

    val (searchText, setSearchText) = remember { mutableStateOf("") }
    val allSalas = listOf("Sala J", "Sala I", "Sala L", "Sala F", "Sala 218")
    val (filteredSalas, setFilteredSalas) = remember { mutableStateOf(allSalas) }
    LaunchedEffect(Unit) {
        viewModel.fetchRooms()
>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically, // Centrar elementos verticalmente
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Logo de la Universidad (Imagen más grande)
                        Image(
<<<<<<< HEAD
                            painter = painterResource(id = R.drawable.logo_blanco_ucaldas_recortado),
=======
                            painter = painterResource(id = R.drawable.login),
>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
                            contentDescription = "Logo Universidad",
                            modifier = Modifier
                                .size(250.dp) // Aumenta el tamaño de la imagen
                                .weight(1f, fill = false) // Asegúrate de que no ocupe más espacio del necesario
                        )

                        // Texto (SIS)
                        Text(
                            text = "SIS",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .weight(1f, fill = false) // Evita que el texto se deforme
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
            // Mostrar estado de carga o error
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
<<<<<<< HEAD
            } else {
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
                            },
                            placeholder = { Text(text = "ej: sala j", color = Color.Black) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color(0xFFD9D9D9),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { // Filtrar al hacer clic
                                // Restablecer filteredSalas a allSalas antes de filtrar
                                filteredSalas = allSalas // Asegúrate de que tengas todas las salas disponibles
                                filterSalas(searchText, allSalas, setFilteredSalas = {
                                    filteredSalas = it
                                })
                            },
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
                            .padding(8.dp)
                            .clickable {
                                // Navegar a la vista de detalles de la sala
                                navController.navigate("detalleSala/${sala.id}") // Asumiendo que `sala.id` es único
                            },
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
                                    text = sala.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = sala.description ?: "Sin descripción",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
=======
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

>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
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
    allSalas: List<Room>,
    setFilteredSalas: (List<Room>) -> Unit
) {
    val filteredSalas = if (searchText.isEmpty()) {
        allSalas // Si el texto está vacío, devuelve todas las salas
    } else {
        allSalas.filter { sala ->
            sala.name.lowercase().contains(searchText.lowercase())
        }
    }
    setFilteredSalas(filteredSalas)
}
<<<<<<< HEAD


=======
>>>>>>> bd99d14f59cd0c7d2cb74b6beec4569155194a8f
