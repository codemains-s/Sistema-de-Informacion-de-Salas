import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.sis.logic.logicUser.getAllUsers
import androidx.compose.ui.unit.sp
import com.example.sis.datamodels.user.UserTable
import com.example.sis.logic.logicUser.AllUsersResult
import android.widget.Toast
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.example.app.utils.UserReportResult
import com.example.app.utils.downloadUserReport
import com.example.sis.R
import com.example.sis.logic.logicUser.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarUsuariosView(
    navController: NavController,
) {
    val (searchText, setSearchText) = remember { mutableStateOf("") }
    var allUsers by remember { mutableStateOf<List<UserTable>>(emptyList()) }
    var filteredUsers by remember { mutableStateOf<List<UserTable>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val result = getAllUsers(context)
                when (result) {
                    is AllUsersResult.Success -> {
                        allUsers = result.users
                        filteredUsers = allUsers
                    }
                    is AllUsersResult.Error -> {
                        errorMessage = result.message
                    }
                }

            } catch (e: Exception) {
                errorMessage = "Error al cargar las usuarios: ${e.message}"
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
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xffffffff))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            TextField(
                                value = searchText,
                                onValueChange = { newText ->
                                    setSearchText(newText)
                                },
                                placeholder = { Text(text = "ej: jhair", color = Color.Black) },
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
                                    filterUsers(searchText, allUsers) {
                                        filteredUsers = it
                                    }
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
                }

                filteredUsers.forEach { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF2C663)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Nombre: ")
                                    }
                                    append(user.name)
                                },
                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Correo: ")
                                    }
                                    append(user.email)
                                },
                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("Programa: ")
                                    }
                                    append(user.program)
                                },
                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        val authHeader = "Bearer ${TokenManager(context).getToken()}"
                                        val result = downloadUserReport(context, userId = user.id.toString(), authHeader = authHeader)

                                        when (result) {
                                            is UserReportResult.Success -> {
                                                println(result.filePath)
                                                Toast.makeText(context, "Archivo guardado en: ${result.filePath}", Toast.LENGTH_LONG).show()
                                            }
                                            is UserReportResult.Error -> {
                                                Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A5795),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.download_icon),
                                    contentDescription = "Descargar Reporte",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(text = "Descargar Reporte")
                            }
                            Button(
                                onClick = {
                                    navController.navigate("listarReservas")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A5795),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text(text = "Asignar Sala")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun filterUsers(
    searchText: String,
    allUsers: List<UserTable>,
    setFilteredUsers: (List<UserTable>) -> Unit
) {
    val filteredUsers = allUsers.filter { user ->
        (searchText.isEmpty() || user.name.lowercase().contains(searchText.lowercase()))
    }
    setFilteredUsers(filteredUsers)
}

