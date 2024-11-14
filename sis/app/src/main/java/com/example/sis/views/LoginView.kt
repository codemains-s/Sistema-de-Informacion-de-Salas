package com.example.sis.views

import androidx.compose.animation.VectorConverter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.sis.R
import com.example.sis.conexion_api.ApiService
import com.example.sis.logic.user.logicUser.LoginResult
import com.example.sis.logic.user.logicUser.loginUser
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
                .fillMaxSize()
                .zIndex(1f),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f),
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

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            when (val result = loginUser(email, password)) {
                                is LoginResult.Success -> {
                                    token = result.token
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
                    Column {
                        Text("Has iniciado sesión correctamente")
                        Text(
                            text = "Token: ${token.take(20)}...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
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
fun ErrorDialogLogin(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Error") },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}

@Composable
fun SuccessDialogLogin(navController: NavController, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "successful login") },
        text = { Text(text = "you have logged in successfully") },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            ) {
                Text("Continue")
            }
        }
    )
}