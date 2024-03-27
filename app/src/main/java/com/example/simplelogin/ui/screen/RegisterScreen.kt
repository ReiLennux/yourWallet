import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.simplelogin.R
import com.example.simplelogin.data.model.User
import com.example.simplelogin.viewmodel.AuthViewModel
import com.google.firebase.database.FirebaseDatabase
import java.util.*

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registrate !",
            modifier = Modifier.padding(bottom = 16.dp),
            fontSize = 35.sp
        )
        val fullName = LocalContext.current.getString(R.string.full_name)
        val email = LocalContext.current.getString(R.string.email)
        val password = LocalContext.current.getString(R.string.password)
        val confirmPassword = LocalContext.current.getString(R.string.confirm_password)
        val curp = LocalContext.current.getString(R.string.curp)

        var fullNameText by rememberSaveable { mutableStateOf("") }
        var emailText by rememberSaveable { mutableStateOf("") }
        var passwordText by rememberSaveable { mutableStateOf("") }
        var confirmPasswordText by rememberSaveable { mutableStateOf("") }
        var curpText by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = fullNameText,
            onValueChange = { fullNameText = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = emailText,
            onValueChange = { emailText = it },
            label = { Text("Correo Electronico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // Oculta la contraseña
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )
        OutlinedTextField(
            value = confirmPasswordText,
            onValueChange = { confirmPasswordText = it },
            label = { Text("Nuevamente su contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(), // Oculta la contraseña
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),

            )
        Spacer(modifier = Modifier.padding(14.dp))
        Text(
            text = "Para usar nuestra aplicación necesitamos validar tu identidad mediante el CURP",
            modifier = Modifier
                .padding(top = 21.dp),
            fontSize = 10.sp, // Tamaño del texto ajustado
            color = LocalContentColor.current.copy(alpha = 0.4f), // Color de texto más claro
            fontStyle = FontStyle.Italic, // Texto en cursiva
            lineHeight = 14.sp,
            fontWeight = FontWeight.Thin
        )
        OutlinedTextField(
            value = curpText,
            onValueChange = { curpText = it },
            label = { Text("CURP") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Ya tengo una cuenta", modifier = Modifier.padding(10.dp)
            .clickable { navController.navigate("login") }
            ,style = MaterialTheme.typography.bodySmall)

        Button(
            onClick = {
                // Firebase Realtime Database code
                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("users")

                // Genera una clave única para el usuario
                val userId = UUID.randomUUID().toString()

                val newUser = User(
                    userId,
                    emailText,
                    fullNameText,
                    curpText,
                    passwordText,
                    cards = null
                )

                usersRef.child(userId).setValue(newUser).addOnSuccessListener {
                    // Registro exitoso
                    showDialog = true
                    dialogMessage = "Registro exitoso"
                    authViewModel.setUserId(userId) // Aquí utilizamos authViewModel
                    navController.navigate("CreditCardScreen")
                }.addOnFailureListener {
                    // Registro fallido
                    showDialog = true
                    dialogMessage = "Registro fallido"
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Registrarse")
        }
    }
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
