// Importa las clases necesarias
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplelogin.components.AppBar
import com.example.simplelogin.data.model.Card
import com.example.simplelogin.viewmodel.AuthViewModel
import com.google.firebase.database.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditCardScreen(navController: NavController, authViewModel: AuthViewModel) {
    var cards by remember { mutableStateOf<List<Card>>(emptyList()) }
    val database = FirebaseDatabase.getInstance().reference
    var userId by remember { mutableStateOf<String?>(null) }

    // Observa el ID del usuario en el ViewModel
    LaunchedEffect(authViewModel) {
        userId = authViewModel.getUserId()
        // Si el ID del usuario está disponible, entonces busca las tarjetas del usuario
        if (!userId.isNullOrEmpty()) {
            fetchCards(userId!!, database) { fetchedCards ->
                cards = fetchedCards
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // AppBar con el nombre de la aplicación y el botón de logout
        AppBar(navController = navController)

        // Espacio entre el contenido y el botón
        Column(modifier = Modifier.weight(1f)) {
            // Texto que muestra el ID del usuario
            Text(
                text = "Buenos Dias! ⛅",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp)
            )

            // Mostrar las tarjetas del usuario solo si el ID del usuario está disponible
            if (!userId.isNullOrEmpty()) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(cards) { card ->
                        CreditCardItem(creditCard = card)
                    }
                }
            }
        }

        // Botón en la esquina inferior derecha
        // Envolver el FilledIconButton en un Box con contentAlignment
        Box(
            modifier = Modifier.fillMaxWidth().background(color = Color.Transparent),
            contentAlignment = Alignment.BottomEnd, // Alineación en esquina inferior derecha
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate("RegisterCardScreen")
                },
                modifier = Modifier
                    .padding(20.dp)
                    .size(70.dp)

                    ,
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar tarjeta",
                )
            }
        }

    }
}

// Función para recuperar las tarjetas del usuario desde la base de datos
private fun fetchCards(
    userId: String,
    database: DatabaseReference,
    onCardsFetched: (List<Card>) -> Unit
) {
    val userRef = database.child("users").child(userId).child("cards")
    userRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val cardList = mutableListOf<Card>()
            for (snapshot in dataSnapshot.children) {
                val card = snapshot.getValue(Card::class.java)
                card?.let { cardList.add(it) }
            }
            onCardsFetched(cardList)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle errors
        }
    })
}

@Composable
fun CreditCardItem(creditCard: Card) {
    // Variable para mantener el estado de si se ha copiado el número de tarjeta
    var copied by remember { mutableStateOf(false) }

    // Función para copiar el número de tarjeta al portapapeles
    val context = LocalContext.current
    val copyToClipboard: (String) -> Unit = { text ->
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboardManager.setPrimaryClip(clip)
        copied = true

        // Mostrar un Toast para confirmar que el texto se ha copiado
        Toast.makeText(context, "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }

    // Diseño para cada tarjeta de crédito (personalizar según sea necesario)
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                copyToClipboard(creditCard.cardNumber)
            }, // Agregar clickable para copiar al portapapeles
        border = BorderStroke(1.dp, Color.Gray),
        shape = MaterialTheme.shapes.small
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Número de tarjeta:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = creditCard.cardNumber,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dirección:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = creditCard.address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,

            ) {
                Column(modifier = Modifier.padding(end = 20.dp)){
                    Text(
                        text = "Vencimiento:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = creditCard.expirationDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Column {
                    Text(
                        text = "CVV:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = creditCard.cvv,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}


