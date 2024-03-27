import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.example.simplelogin.R
import com.example.simplelogin.MainActivity

class RegisterActivity : Activity() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()

        // Crear vistas programáticamente
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val fullNameEditText = EditText(this)
        fullNameEditText.hint = "Full Name"
        layout.addView(fullNameEditText)

        val emailEditText = EditText(this)
        emailEditText.hint = "Email"
        layout.addView(emailEditText)

        val passwordEditText = EditText(this)
        passwordEditText.hint = "Password"
        passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(passwordEditText)

        val registerButton = Button(this)
        registerButton.text = "Register"
        registerButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            println("Full Name: $fullName") // Depuración: Verifica si se obtiene el nombre completo correctamente
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Guardar datos del usuario en la base de datos en tiempo real
            saveUserToDatabase(fullName, email, password)
        }
        layout.addView(registerButton)

        setContentView(layout)
    }

    private fun saveUserToDatabase(fullName: String, email: String, password: String) {
        val usersRef = database.getReference("users")
        val userId = usersRef.push().key
        println("User ID: $userId") // Depuración: Verifica si se genera el ID de usuario correctamente
        val user = mapOf(
            "fullName" to fullName,
            "email" to email,
            "password" to password
        )

        if (userId != null) {
            usersRef.child(userId).setValue(user).addOnCompleteListener { task ->
                if (true) {
                    // Registro exitoso, redirigir a la actividad principal
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    showToast("Registration successful")
                } else {
                    // Error al registrar
                    showToast("Registration failed")
                }
            }
        } else {
            showToast("Failed to generate user ID")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
