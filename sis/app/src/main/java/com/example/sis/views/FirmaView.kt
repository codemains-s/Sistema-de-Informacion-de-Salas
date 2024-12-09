package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.R

@Composable
fun FirmaView(
    navController: NavController,
) {
    var path by remember { mutableStateOf(Path()) }
    var points by remember { mutableStateOf(listOf<Offset>()) }
    var shouldRedraw by remember { mutableStateOf(false) }

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
                painter = painterResource(id = R.drawable.ucaldas_fondo3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .width(350.dp)
                        .fillMaxHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF2C663)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Firmas",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Canvas para dibujar la firma
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .pointerInput(Unit) {
                                    detectDragGestures(
                                        onDragStart = { position ->
                                            points = listOf(position) // Reiniciar los puntos
                                        },
                                        onDrag = { change, _ ->
                                            points = points + change.position
                                        },
                                        onDragEnd = {
                                            // Al finalizar, añadir los puntos al Path
                                            path = path.apply {
                                                if (points.isNotEmpty()) {
                                                    moveTo(points.first().x, points.first().y)
                                                    points.drop(1).forEach {
                                                        lineTo(it.x, it.y)
                                                    }
                                                }
                                            }
                                            points = emptyList() // Reiniciar puntos
                                        }
                                    )
                                }
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // Dibujar el Path completo acumulado
                                drawPath(
                                    path = path,
                                    color = Color.Black,
                                    style = Stroke(
                                        width = 3.dp.toPx(),
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )

                                // Dibujar las líneas temporales mientras se arrastra
                                if (points.isNotEmpty()) {
                                    for (i in 0 until points.size - 1) {
                                        drawLine(
                                            color = Color.Black,
                                            start = points[i],
                                            end = points[i + 1],
                                            strokeWidth = 3.dp.toPx(),
                                            cap = StrokeCap.Round
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botones para guardar o limpiar
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(onClick = {
                                // Implementa la lógica para guardar la firma
                            }) {
                                Text("Guardar")
                            }
                            Button(onClick = {
                                points = emptyList() // Limpiar puntos
                                path = Path() // Reiniciar el Path
                                shouldRedraw = !shouldRedraw // Forzar la redibujación
                            }) {
                                Text("Limpiar")
                            }
                        }
                    }
                }
            }
        }
    }
}