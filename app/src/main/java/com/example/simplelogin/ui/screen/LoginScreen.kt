import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.simplelogin.data.model.User
import com.example.simplelogin.viewmodel.AuthViewModel
import com.google.firebase.database.*

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    val database = FirebaseDatabase.getInstance()
    val usersRef = database.getReference("users")

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isLoginSuccessful by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido a", style = MaterialTheme.typography.titleMedium)
        Text(text = "yourWallet \uD83D\uDCB5", modifier = Modifier.padding(bottom = 10.dp), style = MaterialTheme.typography.headlineLarge)

        Text(text = "Por favor inicia Sesion", modifier = Modifier.padding(bottom = 10.dp), style = MaterialTheme.typography.bodySmall)

        var emailText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }

        OutlinedTextField(
            value = emailText,
            onValueChange = { emailText = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation() // Oculta el texto con puntos
        )
        Text(text = "No tienes cuenta? Crea una.", modifier = Modifier.padding(10.dp)
            .clickable { navController.navigate("register") }
            ,style = MaterialTheme.typography.bodySmall)


        Button(onClick = {
            if (validateInputs(emailText, passwordText)) {
                // Verificar credenciales del usuario en Firebase Realtime Database
                usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userMap = snapshot.value as? HashMap<String, Any>
                        if (userMap != null) {
                            val user = userMap.values.find {
                                val userData = it as HashMap<*, *>
                                userData["email"] == emailText
                            } as? HashMap<*, *>
                            if (user != null) {
                                val storedPassword = user["password"] as? String
                                if (storedPassword != null && storedPassword == passwordText) {
                                    // Inicio de sesión exitoso
                                    showDialog = true
                                    dialogMessage = "Bienvenido de nuevo !"
                                    // Guardar el ID del usuario en el ViewModel
                                    authViewModel.setUserId(user["uid"] as String)
                                    // Indicar que el inicio de sesión fue exitoso
                                    isLoginSuccessful = true
                                    return
                                }
                            }
                        }
                        // Error en el inicio de sesión
                        showDialog = true
                        dialogMessage = "Credenciales incorrectas"
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Manejo de errores en la consulta de la base de datos
                        showDialog = true
                        dialogMessage = "Error: ${error.message}"
                        isLoginSuccessful = false // Se establece a falso si ocurre un error de base de datos
                    }
                })
            } else {
                // Mostrar mensaje de error si la validación falla
                showDialog = true
                dialogMessage = "Por favor, ingresa un correo electrónico válido y una contraseña con al menos 6 caracteres."
            }
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text("Iniciar Sesion")

        }
    }

    if (showDialog) {
        // Diálogo para mostrar mensajes al usuario
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Ey...") },
            confirmButton = { false },
            text = {
                Text(dialogMessage)
            }
        )
    }

    // Navegar a la pantalla de tarjetas de crédito si el inicio de sesión fue exitoso
    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful) {
            // Introducir una pausa de 500 milisegundos antes de navegar
            navController.navigate("CreditCardScreen")
        }
    }
}

// Función para validar correo electrónico y contraseña
fun validateInputs(email: String, password: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6
}

