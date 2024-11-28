package com.example.sis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sis.logic.logicUser.TokenManager
import com.example.sis.ui.theme.SISTheme
import com.example.sis.views.DetalleSalaView
import com.example.sis.views.HorayRegisterView
import com.example.sis.views.ListarReservasSalaView
import com.example.sis.views.ListarSalaView
import com.example.sis.views.RegisterView
import com.example.sis.views.LoginView
import com.example.sis.views.PerfilView
import com.example.sis.views.ProgramListView
import com.example.sis.views.RegistrarHorasView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SISTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("login") {
                        LoginView(navController = navController)
                    }
                    composable("register") {
                        RegisterView(navController = navController)
                    }
                    composable("listarSalas"){ ListarSalaView(navController = navController) }
                    composable("reservarSala/{roomID}/{userID}") { backStackEntry ->
                        val roomID = backStackEntry.arguments?.getString("roomID")?.toIntOrNull()
                        val userID = backStackEntry.arguments?.getString("userID")?.toIntOrNull()
                        if (roomID != null) {
                            HorayRegisterView(navController = navController, roomId = roomID)
                        }
                    }
                    composable("registrarHoras"){ RegistrarHorasView(navController = navController) }

                    composable("detalleSala/{salaId}") { backStackEntry ->
                        val salaId = backStackEntry.arguments?.getString("salaId")
                        if (salaId != null) {
                            DetalleSalaView(salaId, navController)
                        }
                    }
                    composable("profile/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")
                        if (userId != null) {
                            PerfilView(userId = userId, navController = navController)
                        }
                    }
                    composable ("programList"){
                        ProgramListView(navController= navController)
                    }
                    composable ("listarReservas"){
                        ListarReservasSalaView(navController = navController)
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    SISTheme {
        val navController = rememberNavController()
        MainScreen(navController = navController)
    }
}

@Composable
fun MainScreen(navController: NavController) {
    val context = LocalContext.current
    var hasActiveSession by remember { mutableStateOf(false) }
    val tokenManager = TokenManager(context)

    LaunchedEffect(Unit) {
        val savedToken = tokenManager.getToken()
        hasActiveSession = !savedToken.isNullOrEmpty()
    }
    if (hasActiveSession) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(3f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Espacio vacío a la izquierda para mantener el botón a la derecha
            Spacer(modifier = Modifier.weight(1f))

            // Botón de navegación con flecha futurista
            IconButton(
                onClick = {
                    navController.navigate("listarSalas") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .background(
                        color = Color.Transparent, // Sin fondo
                        shape = CircleShape
                    )
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.flecha), // Cambia por tu recurso SVG
                    contentDescription = "Continuar",
                    tint = Color.Unspecified, // Sin color para mantener los detalles originales del SVG
                    modifier = Modifier.size(32.dp) // Ajusta el tamaño según lo necesario
                )
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3))
    ) {
        Image(
            painter = painterResource(id = R.drawable.ucaldas_fondo2),
            contentDescription = "Fondo principal",
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            contentScale = ContentScale.Crop,
            alpha = 0.3f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome to SIS",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Button(
                onClick = { navController.navigate("register") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF2196F3)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                )
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Text(
                text = "© 2024 SIS. All rights reserved.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(top = 48.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

