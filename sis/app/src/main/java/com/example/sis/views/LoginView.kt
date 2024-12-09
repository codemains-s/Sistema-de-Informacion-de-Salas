package com.example.sis.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.R
import com.example.sis.conexion_api.ApiService
import com.example.sis.logic.logicUser.TokenManager
import com.example.sis.logic.user.logicUser.LoginResult
import com.example.sis.logic.user.logicUser.loginUser
import kotlinx.coroutines.launch

@Composable
fun LoginView(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var token by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3))
    ) {
        Image(
            painter = painterResource(id = R.drawable.ucaldas_fondo2),
            contentDescription = "Fondo de registro",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login to SIS",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val textFieldShape = RoundedCornerShape(24.dp)
                val textFieldColors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.9f),
                    focusedContainerColor = Color.Black,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )

                Image(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = "Fondo de registro",
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors,
                    enabled = !isLoading
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))
                val tokenManager = TokenManager(context)
                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            when (val result = loginUser(email, password, context)) {
                                is LoginResult.Success -> {
                                    token = result.token
                                    tokenManager.saveToken(token)
                                    userId = result.userId
                                    showSuccessDialog = true
                                    ApiService.setAuthToken(result.token)
                                }
                                is LoginResult.Error -> {
                                    errorMessage = when (result.message) {
                                        "User not exist" -> "El usuario no existe"
                                        "Password incorrect" -> "La contraseña es incorrecta"
                                        "Token not exist" -> "Error al generar el token"
                                        else -> result.message
                                    }
                                    showErrorDialog = true
                                }
                            }
                            isLoading = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            "Login",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text(text = "Inicio de sesión exitoso") },
                text = {
                    Text(
                        text = "Has iniciado sesión correctamente",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navController.navigate("profile/${userId}") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Continuar")
                    }
                }
            )
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text(text = "Error de inicio de sesión") },
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

@Composable
fun CheckSession(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val token = tokenManager.getToken()

    LaunchedEffect(Unit) {
        if (!token.isNullOrEmpty()) {
            navController.navigate("listarSalas") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
}

@Composable
fun LogoutButton(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                text = "Cerrar sesión",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Confirmar cierre de sesión") },
                text = { Text(text = "¿Estás seguro de que deseas cerrar sesión?") },
                confirmButton = {
                    Button(
                        onClick = {
                            tokenManager.clearToken()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cerrar sesión")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
