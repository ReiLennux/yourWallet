import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.simplelogin.R
import com.example.simplelogin.components.AppBar
import com.example.simplelogin.data.model.Card
import com.example.simplelogin.viewmodel.AuthViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlin.random.Random

@Composable
fun RegisterCardScreen(navController: NavController, authViewModel: AuthViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // Obtenemos la referencia a la base de datos de Firebase
    val database = FirebaseDatabase.getInstance()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppBar(navController = navController)

        // Espacio entre el contenido y el botón
        Column(
            modifier = Modifier.weight(1f)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ingresa los datos de la tarjeta \uD83D\uDCB3",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp),
                fontSize = 35.sp
            )
            val cardNumber = LocalContext.current.getString(R.string.cardNumber)
            val expirationDate = LocalContext.current.getString(R.string.expirationDate)
            val cvv = LocalContext.current.getString(R.string.cvv)
            val bank = LocalContext.current.getString(R.string.bank)
            val address = LocalContext.current.getString(R.string.address)

            var cardNumberText by rememberSaveable { mutableStateOf("") }
            var expirationDateText by rememberSaveable { mutableStateOf("") }
            var cvvText by rememberSaveable { mutableStateOf("") }
            var bankText by rememberSaveable { mutableStateOf("") }
            var addressText by rememberSaveable { mutableStateOf("") }

            OutlinedTextField(
                value = cardNumberText,
                onValueChange = { cardNumberText = it },
                label = { Text("Numero de tarjeta") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = expirationDateText,
                onValueChange = { expirationDateText = it },
                label = { Text("Fecha de Vencimiento") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = cvvText,
                onValueChange = { cvvText = it },
                label = { Text("CVV") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = bankText,
                onValueChange = { bankText = it },
                label = { Text("banco") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = addressText,
                onValueChange = { addressText = it },
                label = { Text("Direccion") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (cardNumberText.isNotEmpty() && expirationDateText.isNotEmpty() &&
                        cvvText.isNotEmpty() && bankText.isNotEmpty() && addressText.isNotEmpty()
                    ) {
                        // Generamos un balance aleatorio entre 1000 y 10000
                        val balance = Random.nextDouble(1000.0, 10000.0)

                        // Creamos un objeto Card con los datos proporcionados
                        val card = Card(
                            cardNumberText,
                            expirationDateText,
                            cvvText,
                            balance,
                            addressText,
                            bankText
                        )

                        // Obtenemos el ID del usuario desde el ViewModel
                        val userId = authViewModel.getUserId()
                        if (userId != null) {
                            // Obtenemos una referencia al nodo del usuario actual y guardamos la tarjeta en él
                            val userRef = database.getReference("users/$userId/cards")
                            val newCardRef = userRef.push()
                            newCardRef.setValue(card).addOnSuccessListener {
                                // Registro exitoso
                                showDialog = true
                                dialogMessage = "Registro de tarjeta exitoso"
                            }.addOnFailureListener {
                                // Registro fallido
                                showDialog = true
                                dialogMessage = "Registro de tarjeta fallido"
                            }
                        } else {
                            showDialog = true
                            dialogMessage = "no hay usuario"
                        }
                    } else {
                        showDialog = true
                        dialogMessage = "Por favor, completa todos los campos"
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Guardar Tarjeta")
            }

            // AlertDialog para mostrar mensajes
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Mensaje") },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Aceptar")
                        }
                    },
                    text = {
                        Text(dialogMessage)
                    }
                )
            }

        }
    }
}
