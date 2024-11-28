package com.example.sis.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.sis.R
import com.example.sis.datamodels.program.Program
import com.example.sis.logic.logicProgram.ProgramResult
import com.example.sis.logic.logicProgram.programList
import com.example.sis.logic.user.logicUser.RegisterResult
import com.example.sis.logic.user.logicUser.registerUser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    var selectedProgram by remember { mutableStateOf<Program?>(null) }
    var code by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isLoadingPrograms by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var programs by remember { mutableStateOf<List<Program>>(emptyList()) }
    var programLoadError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Dropdown state management
    var expanded by remember { mutableStateOf(false) }

    // Program loading effect
    LaunchedEffect(Unit) {
        isLoadingPrograms = true
        when (val result = programList(context)) {
            is ProgramResult.Success -> {
                programs = result.programs
                programLoadError = null
            }
            is ProgramResult.Error -> {
                programs = emptyList()
                programLoadError = result.message
            }
        }
        isLoadingPrograms = false
    }

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
                .verticalScroll(scrollState)
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

                // Program Dropdown
                if (isLoadingPrograms) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(vertical = 16.dp)
                    )
                } else if (programLoadError != null) {
                    Text(
                        text = programLoadError ?: "Error loading programs",
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextField(
                            value = selectedProgram?.name ?: "Select Program",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Program") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = Color.Black.copy(alpha = 0.9f),
                                focusedContainerColor = Color.Black,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = textFieldShape
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            programs.forEach { program ->
                                DropdownMenuItem(
                                    text = { Text(program.name) },
                                    onClick = {
                                        selectedProgram = program
                                        expanded = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }

                // Rest of the form fields remain the same
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
                    label = { Text("Code of admin") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = textFieldShape,
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            when (val result = registerUser(
                                name,
                                email,
                                birthdate,
                                phone,
                                password,
                                selectedProgram?.id ?: 0,
                                code
                            )) {
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
                    enabled = !isLoading && selectedProgram != null
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

        // Success and Error Dialogs remain the same
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