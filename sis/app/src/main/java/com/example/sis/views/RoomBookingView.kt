package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import android.content.Context
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import com.example.sis.datamodels.RoomBooking
import com.example.sis.logic.logicRoom.RoomBookingResult
import com.example.sis.logic.logicRoom.roomBookingList
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.sis.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarReservasSalaView(
    navController: NavController,
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    var allRoomBookings by remember { mutableStateOf<List<RoomBooking>>(emptyList()) }
    var filteredRoomBookings by remember { mutableStateOf<List<RoomBooking>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Cargar los datos de las reservas de sala cuando la vista se lance
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                errorMessage = null

                // Consumir el endpoint para obtener las reservas
                val result = roomBookingList(context)
                when (result) {
                    is RoomBookingResult.Success -> {
                        allRoomBookings = result.rooms
                        filteredRoomBookings = allRoomBookings
                    }
                    is RoomBookingResult.Error -> {
                        errorMessage = result.message
                    }
                }

            } catch (e: Exception) {
                errorMessage = "Error al cargar las reservas: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = { CustomTopAppBar(navController) },
        bottomBar = { CustomBottomAppBar(navController) }
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
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(10.dp),
                    color = Color(0xFFF2C663)
                )
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                filteredRoomBookings.forEach { booking ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2C663)),
                        //shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(15.dp)
                        ) {
                            // Imagen de la reserva
                            Image(
                                painter = painterResource(id = R.drawable.register),
                                contentDescription = "Imagen de la reserva",
                                modifier = Modifier.size(100.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Fecha: ${booking.booking_date}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Horario: ${booking.start_time} - ${booking.end_time}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "Estado: ${booking.status}",
                                    fontSize = 14.sp,
                                    color = if (booking.status.lowercase() == "confirmado") Color.Green else Color.Red
                                )
                            }
                            Button(
                                onClick = {

                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A5795)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(start = 8.dp)
                            ) {
                                Text(text = "Cancelar Reserva")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Función para cancelar la reserva
private suspend fun cancelBooking(
    bookingId: Int,
    context: Context,
    onSuccess: () -> Unit
) {
    try {
        // Aquí implementa la lógica de cancelación, por ejemplo, llamando a un endpoint del backend
        // Simulación de éxito
        onSuccess()
    } catch (e: Exception) {
        // Manejar error
        Log.e("CancelBooking", "Error cancelando la reserva: ${e.message}")
    }
}



