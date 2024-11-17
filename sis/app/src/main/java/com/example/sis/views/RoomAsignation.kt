package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.R
import com.example.sis.logic.logicRoom.RegisterBookingResult
import com.example.sis.logic.logicRoom.registerBooking
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun HorayRegisterView(
    navController: NavController,
    roomId: Int,
    userId: Int,
) {
    // Variables de estado para las cajas de texto
    var programa by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }

    // Variables para el selector de fecha
    var selectedDate by remember { mutableStateOf("") }
    val calendar = Calendar.getInstance()
    val context = LocalContext.current

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Función para mostrar el DatePickerDialog
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Función para validar y ajustar la hora seleccionada
    fun validateTime(hour: Int, minute: Int): Pair<Int, Int> {
        return when {
            hour < 6 -> 6 to 0  // Si es antes de las 6 AM
            hour > 20 -> 20 to 0  // Si es después de las 8 PM
            else -> hour to minute  // Si está dentro del rango
        }
    }

    val timePickerDialogInicio = TimePickerDialog(
        context,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            // Valida el rango de tiempo y ajusta el formato militar
            val (validatedHour, validatedMinute) = validateTime(hourOfDay, minute)
            horaInicio = String.format("%02d:%02d", validatedHour, validatedMinute) // Formato militar
        },
        6, 0, true // El último parámetro en true indica formato 24 horas
    )

    val timePickerDialogFin = TimePickerDialog(
        context,
        { _: TimePicker, hourOfDay: Int, minute: Int ->
            // Valida el rango de tiempo y ajusta el formato militar
            val (validatedHour, validatedMinute) = validateTime(hourOfDay, minute)
            horaFin = String.format("%02d:%02d", validatedHour, validatedMinute) // Formato militar
        },
        6, 0, true // El último parámetro en true indica formato 24 horas
    )

    Scaffold(
        topBar = {CustomTopAppBar(navController)},
        bottomBar = {CustomBottomAppBar(navController)}

    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Fondo de la pantalla
            Image(
                painter = painterResource(id = R.drawable.ucaldas_fondo3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight(), // Altura para agregar el nuevo campo
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
                            text = "Asignar sala",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(text = "Programa", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                        OutlinedTextField(
                            value = programa,
                            onValueChange = { programa = it },
                            modifier = Modifier
                                .height(45.dp)
                                .fillMaxWidth()
                                .background(Color.Transparent, shape = RoundedCornerShape(50.dp)), // Fondo transparente y esquinas redondeadas
                            textStyle = TextStyle(color = Color.Black),
                            shape = RoundedCornerShape(50.dp) // Bordes circulares
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(text = "Nombre", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            modifier = Modifier
                                .height(45.dp)
                                .fillMaxWidth()
                                .background(Color.Transparent, shape = RoundedCornerShape(50.dp)), // Fondo transparente y esquinas redondeadas
                            textStyle = TextStyle(color = Color.Black),
                            shape = RoundedCornerShape(50.dp)
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                        Text(text = "Día", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                        OutlinedButton(
                            onClick = { datePickerDialog.show() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = if (selectedDate.isEmpty()) "" else selectedDate, color= Color.Black)
                        }
                        Spacer(modifier = Modifier.height(15.dp))

                        // Contenedor horizontal para Hora Inicio y Hora Fin
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Hora inicio", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                                OutlinedButton(
                                    onClick = { timePickerDialogInicio.show() },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = if (horaInicio.isEmpty()) "" else horaInicio, color= Color.Black)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Hora fin", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                                OutlinedButton(
                                    onClick = { timePickerDialogFin.show() },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = if (horaFin.isEmpty()) "" else horaFin, color= Color.Black)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    when (val result = registerBooking(
                                        user_id = userId,
                                        room_id = roomId,
                                        booking_date = selectedDate,
                                        start_time = horaInicio,
                                        end_time = horaFin,
                                        status = "En curso..."
                                    )){
                                        is RegisterBookingResult.Success -> {
                                            showSuccessDialog = true
                                        }
                                        is RegisterBookingResult.Error -> {
                                            errorMessage = result.message
                                        }
                                    }
                                    isLoading = false
                                }

                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A5795)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp)
                        ) {
                            Text(
                                text = "Registrar",
                                color = Color(0xFFF2C663)
                            )
                        }
                    }
                }
            }
        }
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(text = "Error al registrarse") },
                text = { Text(text = errorMessage) },
                confirmButton = {
                    Button(onClick = { showErrorDialog = false }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}