package com.example.sis.views

import CustomBottomAppBar
import CustomTopAppBar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sis.ui.theme.SISTheme

@Composable
fun RegistrarHorasView(
    navController: NavController,
    userName: String
) {
    var path by remember { mutableStateOf(Path()) }
    var points by remember { mutableStateOf(listOf<Offset>()) }
    var shouldRedraw by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
    }

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
                painter = painterResource(id = R.drawable.ucaldas_fondo3), // Aquí se debe ingresar la imagen de fondo
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
                        .fillMaxHeight(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF2C663)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Registrar horas cumplidas",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Campos para el nobmre de usuario
                        Text(
                            text = "Nombre",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        Text(
                            text = userName,
                            color = Color.Gray,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                        // Text de horas cumplidas
                        var horasCumplidas by remember { mutableStateOf("0") } // Inica con valor de 0

                        Text(
                            text = "Horas cumplidas",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        OutlinedTextField(
                            value = horasCumplidas,
                            onValueChange = { newValue ->
                                // Si el nuevo valor está vacío, coloca "0"
                                horasCumplidas = when {
                                    newValue.isEmpty() -> "0"
                                    newValue.all { it.isDigit() } && newValue.length <= 3 -> {
                                        // Si el valor actual es "0", reemplázalo con el nuevo valor numérico ingresado
                                        if (horasCumplidas == "0") newValue.trimStart('0') else newValue
                                    }

                                    else -> horasCumplidas // Mantén el valor actual si no cumple con las condiciones
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number // Solo muestra el teclado numérico
                            )
                        )
                        Spacer(modifier = Modifier.height(15.dp))


                        // Text para descripcion
                        var descripcion by remember { mutableStateOf("") }
                        Text(
                            text = "Descripción",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start)
                        )
                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            modifier = Modifier.fillMaxWidth()
                            /*
                             colors = OutlinedTextFieldDefaults.colors(
                                 focusedBorderColor = Color(0xFFF2C663),
                                 unfocusedBorderColor = Color.Gray
                             )*/
                        )
                        Spacer(modifier = Modifier.height(15.dp))


                        /*
                            Este es un campo de pruebas modificar
                         */
                        var isChecked by remember { mutableStateOf(false) }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Completado",
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFFF2C663),
                                    uncheckedColor = Color.Gray
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        /*Text(
                            text = "Firmas",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))*/

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
                                                    points
                                                        .drop(1)
                                                        .forEach {
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

                        // limpiar
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(
                                onClick = {
                                    points = emptyList() // Limpiar puntos
                                    path = Path() // Reiniciar el Path
                                    shouldRedraw = !shouldRedraw // Forzar la redibujación
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0A5795),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Text(text = "Borrar firma")
                            }
                        }

                        // Botón final para confirmar el registro
                        Button(
                            onClick = { /* Reemplaza con la lógica del botón */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A5795) // Color del encabezado
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp)
                        ) {
                            Text(
                                text = "Registrar",
                                color = Color(0xFFF2C663)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenScreen() {
    SISTheme {
        val navController = rememberNavController()
        RegistrarHorasView(navController = navController, userName = "")
    }
}