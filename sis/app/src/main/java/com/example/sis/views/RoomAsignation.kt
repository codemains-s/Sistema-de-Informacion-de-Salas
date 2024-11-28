package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import UserIdManager
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.R
import com.example.sis.datamodels.room.Room
import com.example.sis.datamodels.user.User
import com.example.sis.logic.logicRoom.RegisterBookingResult
import com.example.sis.logic.logicRoom.RoomResult
import com.example.sis.logic.logicRoom.registerBooking
import com.example.sis.logic.logicRoom.roomById
import com.example.sis.logic.user.logicUser.userById
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun HorayRegisterView(
    navController: NavController,
    roomId: Int

) {
    var userDetails by remember { mutableStateOf<User?>(null) }
    var roomDetails by remember { mutableStateOf<Room?>(null) }
    var isLoadingRoom by remember { mutableStateOf(true) }
    var errorMessageRoom by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val userId = UserIdManager(context).getUserId()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(roomId) {
        coroutineScope.launch {
            try {
                isLoadingRoom = true
                errorMessageRoom = null

                val result = roomById(context, roomId)

                when (result) {
                    is RoomResult.Success -> {
                        roomDetails = result.rooms.firstOrNull()
                    }
                    is RoomResult.Error -> {
                        errorMessageRoom = result.message
                    }
                }
            } catch (e: Exception) {
                errorMessageRoom = "Error al cargar los detalles: ${e.message}"
            } finally {
                isLoadingRoom = false
            }
        }
    }

    LaunchedEffect(userId) {
        coroutineScope.launch {
            try {
                isLoadingRoom = true
                errorMessageRoom = null

                val result = userId?.let { userById(context, it.toInt()) }
                if (result != null) {
                    result.fold(
                        onSuccess = { user ->
                            userDetails = user
                        },
                        onFailure = { error ->
                            errorMessageRoom = error.message
                        }
                    )
                }
            } finally {
                isLoadingRoom = false
            }
        }
    }

    // Variables para la selección de fecha y hora
    var selectedDate by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val calendar = Calendar.getInstance()
    val contextPicker = LocalContext.current

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    fun validateTime(hour: Int, minute: Int): Pair<Int, Int> {
        return when {
            hour < 6 -> 6 to 0 // Si es antes de las 6 AM
            hour > 20 -> 20 to 0 // Si es después de las 8 PM
            else -> hour to minute // Si está dentro del rango
        }
    }

    val timePickerDialogInicio = TimePickerDialog(
        context,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            val (validatedHour, validatedMinute) = validateTime(hourOfDay, minute)
            horaInicio = String.format("%02d:%02d", validatedHour, validatedMinute) // Formato militar
        },
        6, 0, true
    )

    val timePickerDialogFin = TimePickerDialog(
        context,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            val (validatedHour, validatedMinute) = validateTime(hourOfDay, minute)
            horaFin = String.format("%02d:%02d", validatedHour, validatedMinute) // Formato militar
        },
        6, 0, true
    )

    Scaffold(
        topBar = { CustomTopAppBar(navController) },
        bottomBar = { CustomBottomAppBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Fondo de la pantalla
            Image(
                painter = painterResource(id = R.drawable.ucaldas_fondo3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (isLoadingRoom) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFF2C663))
            } else if (errorMessageRoom != null) {
                Text(
                    text = errorMessageRoom ?: "Error desconocido",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        modifier = Modifier
                            .width(300.dp)
                            .wrapContentHeight(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF2C663)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Asignar Sala",
                                color = Color.Black,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(text = "Programa", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                            Text(
                                text = "${userDetails?.program ?: "Desconocida"}",
                                color = Color.Black,
                                fontSize = 12.sp,
                                //fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            Text(text = "Nombre monitor", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                            Text(
                                text = "${userDetails?.name ?: "Sin nombre"}",
                                color = Color.Black,
                                fontSize = 12.sp,
                                //fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            Text(text = "Sala", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                            Text(
                                text = "${roomDetails?.name ?: "Desconocida"}",
                                color = Color.Black,
                                fontSize = 12.sp,
                                //fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(15.dp))

                            Text(text = "Día", color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                            OutlinedButton(
                                onClick = { datePickerDialog.show() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = if (selectedDate.isEmpty()) "" else selectedDate, color = Color.Black)
                            }
                            Spacer(modifier = Modifier.height(15.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Hora inicio", fontWeight = FontWeight.Bold, color = Color.Black)
                                    OutlinedButton(
                                        onClick = { timePickerDialogInicio.show() },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = if (horaInicio.isEmpty()) "" else horaInicio, color = Color.Black)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Hora fin", fontWeight = FontWeight.Bold, color = Color.Black)
                                    OutlinedButton(
                                        onClick = { timePickerDialogFin.show() },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(text = if (horaFin.isEmpty()) "" else horaFin, color = Color.Black)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    scope.launch {
                                        isLoading = true
                                        when (val result = userId?.let {
                                            registerBooking(
                                                user_id = it.toInt(),
                                                room_id = roomId,
                                                booking_date = selectedDate,
                                                start_time = horaInicio,
                                                end_time = horaFin,
                                                status = "En curso..."
                                            )
                                        }) {
                                            is RegisterBookingResult.Success -> {
                                                showSuccessDialog = true
                                                navController.navigate("listarReservas")
                                            }
                                            is RegisterBookingResult.Error -> {
                                                errorMessage = result.message
                                                showErrorDialog = true
                                            }

                                            null -> TODO()
                                        }
                                        isLoading = false
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A5795)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Registrar",
                                    color = Color(0xFFF2C663)
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = {
                                    navController.navigate("listarReservas")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A5795)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ){
                                Text(
                                    text = "Consultar reservas",
                                    color = Color(0xFFF2C663)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


