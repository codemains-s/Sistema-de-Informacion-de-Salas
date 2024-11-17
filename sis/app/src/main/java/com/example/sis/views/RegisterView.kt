package com.example.sis.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.sis.logic.user.logicUser.RegisterResult
import com.example.sis.logic.user.logicUser.registerUser
import kotlinx.coroutines.launch

@Composable
fun RegisterView(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3))  // Fondo azul
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.ucaldas_fondo2),
            contentDescription = "Fondo de registro",
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        // Contenedor principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Register in SIS",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.White
            )

            // Formulario
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
                    painter = painterResource(id = R.drawable.register),
                    contentDescription = "Fondo de registro",
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = birthdate,
                    onValueChange = { birthdate = it },
                    label = { Text("Birthday (yyyy-MM-dd)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("code of admin") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                       scope.launch {
                           isLoading = true
                           when (val result = registerUser(name, email, birthdate, phone, password, code)){
                               is RegisterResult.Success -> {
                                   showSuccessDialog = true
                               }
                               is RegisterResult.Error -> {
                                   errorMessage = when (result.message) {
                                       "User exist" -> "User already exist"
                                       "Email incorrect" -> "Email not valid"
                                       "Password incorect" -> "Password not valid"
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
                            "Register",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text(text = "Registro exitoso") },
                text = {
                    Column {
                        Text("Te Has Registrado sesión correctamente")

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
                        Text("Login")
                    }
                }
            )
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

@Composable
fun SuccessDialog(navController: NavController, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "successful registration in SIS") },
        text = { Text(text = "Your registration has been completed successfully.") },
        confirmButton = {
            Button(
                onClick = {
                    onDismiss()
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            ) {
                Text("Go to login")
            }
        }
    )
}