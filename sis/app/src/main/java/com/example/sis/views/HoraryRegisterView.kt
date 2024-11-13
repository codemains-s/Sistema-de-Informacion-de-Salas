package com.example.sis.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.sis.R
import com.example.sis.ui.theme.SISTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoraryRegisterView() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.register),
                            contentDescription = "Logo Universidad",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            text = "Universidad de Caldas",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                        Text(
                            text = "SIS",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(end = 16.dp, top = 5.dp) // Margin a la derecha
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF0A5795)
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF0A5795)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Home",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Schedule",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ucaldas_fondo2),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ucaldas_fondo1),
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background), // Icono extra
                        contentDescription = "Settings",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
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
                        .height(500.dp),
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
                            text = "Asignar horario",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Etiquetas de color negro sobre los campos
                        Text(text = "Programa", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                        OutlinedTextField(
                            value = "", // Reemplaza con el valor del estado
                            onValueChange = { /* Reemplaza con la lógica de cambio de valor */ },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                        Text(text = "Nombre", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                        OutlinedTextField(
                            value = "", // Reemplaza con el valor del estado
                            onValueChange = { /* Reemplaza con la lógica de cambio de valor */ },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                        Text(text = "Hora inicio", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                        OutlinedTextField(
                            value = "", // Reemplaza con el valor del estado
                            onValueChange = { /* Reemplaza con la lógica de cambio de valor */ },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(15.dp))

                        Text(text = "Hora fin", color = Color.Black, modifier = Modifier.align(Alignment.Start))
                        OutlinedTextField(
                            value = "", // Reemplaza con el valor del estado
                            onValueChange = { /* Reemplaza con la lógica de cambio de valor */ },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(20.dp))

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
                                color = Color(0xFFF2C663) // Color del formulario
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
fun PreviewMainScreen() {
    SISTheme {
        HoraryRegisterView()
    }
}