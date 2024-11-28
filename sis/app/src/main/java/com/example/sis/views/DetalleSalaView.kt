package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import RoomIdManager
import UserIdManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.sis.R
import com.example.sis.datamodels.room.Room
import com.example.sis.logic.logicRoom.RoomResult
import com.example.sis.logic.logicRoom.roomById
import kotlinx.coroutines.launch

@Composable
fun DetalleSalaView(salaId: String, navController: NavController) {
    var roomDetails by remember { mutableStateOf<Room?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var roomIdManager = RoomIdManager(context)

    val userId = UserIdManager(context).getUserId()

    LaunchedEffect(salaId) {
        coroutineScope.launch {
            try {
                isLoading = true
                errorMessage = null
                val id = salaId.toInt()

                val result = roomById(context, id)

                when (result) {
                    is RoomResult.Success -> {
                        roomDetails = result.rooms.firstOrNull()
                        roomDetails?.id?.let { roomIdManager.saveUserId(it.toString()) }
                    }
                    is RoomResult.Error -> {
                        errorMessage = result.message
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error al cargar los detalles: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {CustomTopAppBar(navController)},
        bottomBar = {CustomBottomAppBar(navController)}

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
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(80.dp).padding(10.dp), color = Color(0xFFF2C663))
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Ir para atrás",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = roomDetails?.name ?: "Nombre de la sala",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF2C663)
                    )
                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        val imageUrl = roomDetails?.image
                        Image(
                            painter = rememberImagePainter(data = imageUrl ?: R.drawable.register),
                            contentDescription = "Imagen de la sala",
                            modifier = Modifier
                                .size(350.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.height(0.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Descripción: ")
                                }
                                append("${roomDetails?.description ?: "Sin descripción"}")
                            },
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Estado: ")
                                }
                                append("${roomDetails?.status ?: "Sin estado"}")
                            },
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Capacidad: ")
                                }
                                append("${roomDetails?.capacity ?: "Sin capacidad"}")
                            },
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                val roomID = roomDetails?.id ?: return@Button
                                navController.navigate("reservarSala/$roomID/${userId}")
                            },
                            modifier = Modifier.height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A5795),
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Reservar")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                }
            }
        }
    }
}