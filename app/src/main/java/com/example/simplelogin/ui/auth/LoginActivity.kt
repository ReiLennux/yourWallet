package com.example.simplelogin.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.firebase.database.*
import com.example.simplelogin.R
import com.example.simplelogin.MainActivity
import com.example.simplelogin.data.model.User

class LoginActivity : Activity() {

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance()

        // Crear vistas programáticamente
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val emailEditText = EditText(this)
        emailEditText.hint = "Email"
        layout.addView(emailEditText)

        val passwordEditText = EditText(this)
        passwordEditText.hint = "Password"
        passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        layout.addView(passwordEditText)

        val loginButton = Button(this)
        loginButton.text = "Login"
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Verificar credenciales del usuario
            signIn(email, password)
        }
        layout.addView(loginButton)

        setContentView(layout)
    }

    private fun signIn(email: String, password: String) {
        val usersRef = database.getReference("users")
        val query: Query = usersRef.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.password == password) {
                            // Inicio de sesión exitoso, redirigir a la actividad principal
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                            return
                        }
                    }
                }
                // Credenciales incorrectas o usuario no encontrado
                // Manejar el error adecuadamente
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Manejar error en la base de datos
            }
        })
    }
}
