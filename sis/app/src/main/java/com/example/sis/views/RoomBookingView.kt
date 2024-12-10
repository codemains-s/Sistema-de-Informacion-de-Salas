package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
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
import com.example.sis.logic.logicRoom.RoomBookingResult
import com.example.sis.logic.logicRoom.roomBookingList
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import com.example.sis.R
import com.example.sis.datamodels.RoomBookingId
import com.example.sis.logic.logicRoom.RoomBookingDelete
import com.example.sis.logic.logicRoom.roomBookingDelete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarReservasSalaView(
    navController: NavController,
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    var allRoomBookings by remember { mutableStateOf<List<RoomBookingId>>(emptyList()) }
    var filteredRoomBookings by remember { mutableStateOf<List<RoomBookingId>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedBookingId by remember { mutableStateOf<Int?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var errorCancelMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar los datos de las reservas de sala cuando la vista se lance
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                errorMessage = null
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
        bottomBar = { CustomBottomAppBar(navController) },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF6A00F4), Color(0xFF00D4FF))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (successMessage != null) R.drawable.ic_check_circle else R.drawable.ic_error
                            ),
                            contentDescription = if (successMessage != null) "Éxito" else "Error",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = data.visuals.message,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        },
        content = { innerPadding ->
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
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(15.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.reserva),
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
                                        color = if (booking.status.lowercase() == "confirmado") Color(0xFF228B22) else Color.Red
                                    )
                                }
                                Button(
                                    onClick = {
                                        selectedBookingId = booking.id
                                        showDialog = true
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
    )

    // Popup para confirmar la cancelación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Confirmar Cancelación",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text("¿Estás seguro de que deseas cancelar esta reserva?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            showDialog = false // Cerrar el popup primero
                            selectedBookingId?.let { bookingId ->
                                val result = roomBookingDelete(context, bookingId)
                                when (result) {
                                    is RoomBookingDelete.Success -> {
                                        successMessage = "Reserva cancelada con éxito"
                                        filteredRoomBookings = filteredRoomBookings.filter { it.id != bookingId }
                                    }
                                    is RoomBookingDelete.Error -> {
                                        errorCancelMessage = result.message
                                    }
                                }
                                successMessage?.let { snackbarHostState.showSnackbar(it) }
                                errorCancelMessage?.let { snackbarHostState.showSnackbar(it) }
                            }
                        }
                    }
                ) {
                    Text("Aceptar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}




