package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.R
import com.example.sis.logic.logicProgram.ProgramRegister
import com.example.sis.logic.logicProgram.registerProgram
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle


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
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Fondo de la pantalla
            Image(
                painter = painterResource(id = R.drawable.ucaldas_fondo3), // Cambia por tu imagen de fondo
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()), // Permitir scroll si es necesario
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .width(350.dp)
                        .wrapContentHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF2C663) // Fondo del formulario
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Registrar Programa",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Campo para el Nombre del Programa
                        Text(
                            text = "Nombre del programa",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text
                            ),
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                        // Campo para la Descripción del Programa
                        Text(
                            text = "Descripción del programa",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text
                            ),
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))

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
                                .padding(horizontal = 30.dp),
                            enabled = name.isNotEmpty() && description.isNotEmpty() && !isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A5795)
                            )
                        ) {
                            Text(
                                text = if (isLoading) "Cargando..." else "Registrar",
                                color = Color(0xFFF2C663)
                            )
                        }
                    }
                }
            }

            // Diálogo de éxito
            if (showSuccessDialog) {
                AlertDialog(
                    onDismissRequest = { showSuccessDialog = false },
                    title = { Text(text = "Registro Exitoso") },
                    text = { Text("El programa se ha registrado correctamente.") },
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
                    text = { Text(errorMessage) },
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
