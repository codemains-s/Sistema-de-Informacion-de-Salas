import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sis.R

@Composable
fun CustomBottomAppBar(navController: NavController) {
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
            IconButton(onClick = { navController.navigate("registrarHoras") }) {
                Image(
                    painter = painterResource(id = R.drawable.schedule),
                    contentDescription = "Schedule",
                    modifier = Modifier.size(40.dp)
                )
            }
            IconButton(onClick = { navController.navigate("registrarHoras") }) {
                Image(
                    painter = painterResource(id = R.drawable.notification),
                    contentDescription = "Notifications",
                    modifier = Modifier.size(40.dp)
                )
            }
            IconButton(onClick = { navController.navigate("reservarSala") }) {
                Image(
                    painter = painterResource(id = R.drawable.account),
                    contentDescription = "Profile",
                    modifier = Modifier.size(40.dp)
                )
            }
            IconButton(onClick = { navController.navigate("reservarSala") }) {
                Image(
                    painter = painterResource(id = R.drawable.setting),
                    contentDescription = "Settings",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}