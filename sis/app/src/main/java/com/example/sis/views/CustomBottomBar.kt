import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sis.R

@Composable
fun CustomBottomAppBar(navController: NavController) {
    val context = LocalContext.current
    var roomIdManager = RoomIdManager(context)
    var userIdManager = UserIdManager(context)
    var userRoleIdManager = UserRoleIdManager(context)

    // State for dropdown menu
    var expanded by remember { mutableStateOf(false) }

    BottomAppBar(
        containerColor = Color(0xFF0A5795)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.navigate("listarSalas") }) {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    modifier = Modifier.size(40.dp)
                )
            }

            if (userRoleIdManager.getUserRoleId() == "1") {

                IconButton(onClick = { navController.navigate("listarReservas") }) {
                    Image(
                        painter = painterResource(id = R.drawable.schedule),
                        contentDescription = "Schedule",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            IconButton(onClick = { navController.navigate("profile/${userIdManager.getUserId()}") }) {
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = "Profile",
                    modifier = Modifier.size(40.dp)
                )
            }
            if (userRoleIdManager.getUserRoleId() == "1") {
                Box {
                    IconButton(onClick = { expanded = !expanded }) {
                        Image(
                            painter = painterResource(id = R.drawable.setting),
                            contentDescription = "Settings",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(
                                color = Color(0xFFF2C663),
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .background(
                                    color = Color(0xFF0A5795),
                                    shape = RoundedCornerShape(20.dp)
                                )

                        ) {
                            DropdownMenuItem(
                                colors = MenuDefaults.itemColors(
                                    textColor = Color.White,
                                    leadingIconColor = Color.White
                                ),
                                text = {
                                    Text(
                                        "Registrar Programa",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    navController.navigate("registrarPrograma")
                                    expanded = false
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .background(
                                    color = Color(0xFF0A5795),
                                    shape = RoundedCornerShape(20.dp)
                                )

                        ) {
                            DropdownMenuItem(
                                colors = MenuDefaults.itemColors(
                                    textColor = Color.White,
                                    leadingIconColor = Color.White
                                ),
                                text = {
                                    Text(
                                        "Ver monitores",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    navController.navigate("listarUsuarios")
                                    expanded = false
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .background(
                                    color = Color(0xFF0A5795),
                                    shape = RoundedCornerShape(20.dp)
                                )
                        ) {
                            DropdownMenuItem(
                                colors = MenuDefaults.itemColors(
                                    textColor = Color.White,
                                    leadingIconColor = Color.White
                                ),
                                text = {
                                    Text(
                                        "Otra Opción",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                },
                                onClick = {
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
