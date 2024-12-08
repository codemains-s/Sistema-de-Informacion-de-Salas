package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import android.text.Layout
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.R
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.sis.datamodels.user.User
import com.example.sis.logic.logicUser.userList
import com.example.sis.logic.logicUser.UserResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarUsuarioView(
    navController: NavController,
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    var allUsers by remember { mutableStateOf<List<User>>(emptyList()) }
    var filteredUsers by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val result = userList(context)
                when (result) {
                    is UserResult.Success -> {
                        // Filtrar solo los usuarios con role_id = 1
                        allUsers = result.users.filter { it.role_id == 2 }
                        filteredUsers = allUsers
                    }
                    is UserResult.Error -> {
                        errorMessage = result.message
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error al cargar los usuarios: ${e.message}"
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
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
                            placeholder = { Text(text = "ej: nombre", color = Color.Black) },
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
                                filteredUsers = allUsers
                                filterUsers(searchText, allUsers, setFilteredUsers = {
                                    filteredUsers = it
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

                filteredUsers.forEach { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                navController.navigate("profile/${user.id}")
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF2C663)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(15.dp, 0.dp)
                        ) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = user.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = user.email,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyRowComponentPreview() {
    val navController = rememberNavController()
    ListarUsuarioView(navController)
}

private fun filterUsers(
    searchText: String,
    allUsers: List<User>,
    setFilteredUsers: (List<User>) -> Unit
) {
    val filteredUsers = if (searchText.isEmpty()) {
        allUsers
    } else {
        allUsers.filter { user ->
            user.name.lowercase().contains(searchText.lowercase())
        }
    }
    setFilteredUsers(filteredUsers)
}
