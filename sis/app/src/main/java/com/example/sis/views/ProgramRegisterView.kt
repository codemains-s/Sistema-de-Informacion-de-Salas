package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.logic.logicProgram.ProgramRegister
import com.example.sis.logic.logicProgram.registerProgram
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramRegisterView(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { CustomTopAppBar(navController) },
        bottomBar = { CustomBottomAppBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White) // Fondo blanco para la vista principal
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val textFieldShape = RoundedCornerShape(24.dp)
                val textFieldColors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF2196F3).copy(alpha = 0.9f), // Fondo azul
                    focusedContainerColor = Color(0xFF2196F3), // Fondo azul cuando está enfocado
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )

                Text(
                    text = "Registrar Programa",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF0A5795), // Color para el título
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo para el Nombre del Programa
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo para la Descripción del Programa
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción del programa") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para registrar
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            when (val result = registerProgram(name, description, context)) {
                                is ProgramRegister.Success -> {
                                    showSuccessDialog = true
                                }
                                is ProgramRegister.Error -> {
                                    errorMessage = result.message
                                    showErrorDialog = true
                                }
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = name.isNotEmpty() && description.isNotEmpty() && !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLoading) Color.Gray else Color(0xFF0A5795),
                        contentColor = Color.White
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Registrar")
                    }
                }
            }

            // Diálogo de éxito
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { showSuccessDialog = false },
                    title = { Text(text = "Registro Exitoso") },
                    text = {
                        Column {
                            Text("El programa se ha registrado correctamente.")
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showSuccessDialog = false
                                navController.navigate("programList") {
                                    popUpTo("programRegister") { inclusive = true }
                                }
                            }
                        ) {
                            Text("Aceptar")
                        }
                    }
                )
            }

            // Diálogo de error
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text(text = "Error en el Registro") },
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
}
