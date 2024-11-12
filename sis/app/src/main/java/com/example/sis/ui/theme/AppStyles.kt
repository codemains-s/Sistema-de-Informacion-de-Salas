package com.example.sis.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.sis.R

// Colores personalizados
val DarkBackground = Color(0xFF121212)
val NeonGreen = Color(0xFF00FF00)
val NeonBlue = Color(0xFF00BFFF)

// Fuentes personalizadas
val FuturisticFontFamily = FontFamily.Monospace

// Tipografía personalizada para Material3
val futuristicTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FuturisticFontFamily,
        fontSize = 30.sp,
        color = NeonGreen
    ),
    bodyLarge = TextStyle(
        fontFamily = FuturisticFontFamily,
        fontSize = 16.sp,
        color = Color.White
    )
)

@Composable
fun CustomSISTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = futuristicTypography, // Aplica la tipografía personalizada
        shapes = MaterialTheme.shapes, // Utiliza las formas predeterminadas de MaterialTheme
        content = content
    )
}

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray) // Fondo oscuro si no hay imagen
    ) {
        Image(
            painter = painterResource(id = R.drawable.ucaldas_fondo2), // Reemplaza con tu imagen
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Para que la imagen ocupe toda la pantalla sin distorsionarse
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Aquí van tus campos de entrada, como TextField, etc.
            // Ejemplo de un texto centrado
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge
            )
            // Añadir más campos aquí
        }
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    CustomSISTheme {
        LoginScreen()
    }
}
