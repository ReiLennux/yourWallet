import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplelogin.components.AppBar
import com.example.simplelogin.components.CardComponent
import com.example.simplelogin.data.model.Card
import com.example.simplelogin.viewmodel.AuthViewModel
import com.google.firebase.database.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardScreen(navController: NavController, authViewModel: AuthViewModel) {
    val cardViewModel = remember { CardViewModel(authViewModel) }
    val cards by cardViewModel.cards.collectAsState()

    LaunchedEffect(cardViewModel) {
        cardViewModel.fetchCards()
    }

    Scaffold(
        topBar = {
            // AppBar con el nombre de la aplicación y el botón de logout
            AppBar(navController = navController)
        },
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                // Espacio entre el contenido y el botón
                Column(modifier = Modifier.weight(1f)) {
                    // Texto que muestra el ID del usuario
                    Text(
                        text = "Buenos Dias! ⛅",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Mostrar las tarjetas del usuario
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(cards) { card ->
                            CardComponent(creditCard = card)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("RegisterCardScreen")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(65.dp),
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar tarjeta",
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    )
}
