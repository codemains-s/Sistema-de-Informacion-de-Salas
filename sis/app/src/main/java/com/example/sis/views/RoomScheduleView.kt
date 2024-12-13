package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import UserIdManager
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.example.sis.datamodels.RoomScheduleId
import com.example.sis.logic.logicRoom.RoomScheduleResult
import com.example.sis.logic.logicRoom.roomScheduleListId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarHorariosSalaView(
    id: Int,
    navController: NavController,
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    var allSchedules by remember { mutableStateOf<List<RoomScheduleId>>(emptyList()) }
    var filteredSchedules by remember { mutableStateOf<List<RoomScheduleId>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val userID = UserIdManager(context).getUserId()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val result = roomScheduleListId(id, context)
                when (result) {
                    is RoomScheduleResult.Success -> {
                        allSchedules = result.schedules
                        filteredSchedules = allSchedules
                    }
                    is RoomScheduleResult.Error -> {
                        errorMessage = result.message
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error al cargar los horarios: ${e.message}"
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
                    modifier = Modifier.size(80.dp).padding(10.dp),
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
                            placeholder = { Text(text = "Buscar día o sala", color = Color.Black) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color(0xFFD9D9D9),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            textStyle = LocalTextStyle.current.copy(color = Color.Black),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                filteredSchedules = allSchedules
                                filterSchedules(searchText, allSchedules, setFilteredSchedules = {
                                    filteredSchedules = it
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

                filteredSchedules.forEach { schedule ->
                    val isAvailable = schedule.status == "Disponible"

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF2C663)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Día: ${schedule.day_of_week}",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Horario: ${schedule.hour_start} - ${schedule.hour_end}",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Estado: ${schedule.status}",
                                fontSize = 14.sp,
                                color = if (isAvailable) Color(0xFF228B22) else Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    navController.navigate("reservarSala/${schedule.room_id}/${schedule.id}")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isAvailable) Color(0xFF0A5795) else Color.Gray,
                                    contentColor = Color.White
                                ),
                                enabled = isAvailable
                            ) {
                                Text(text = if (isAvailable) "Reservar Sala" else "No Disponible")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun filterSchedules(
    searchText: String,
    allSchedules: List<RoomScheduleId>,
    setFilteredSchedules: (List<RoomScheduleId>) -> Unit
) {
    val filtered = if (searchText.isEmpty()) {
        allSchedules
    } else {
        allSchedules.filter { schedule ->
            schedule.day_of_week.lowercase().contains(searchText.lowercase()) ||
                    schedule.room_id.toString().contains(searchText)
        }
    }
    setFilteredSchedules(filtered)
}
