package com.example.simplelogin.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.simplelogin.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController) {
    // AppBar con el nombre de la aplicación a la izquierda y el botón de logout a la derecha
    androidx.compose.material3.TopAppBar(
        title = { Text(text = "yourWallet") },
        actions = {
            IconButton(onClick = {
                // Lógica para cerrar sesión y navegar al login
                navController.navigate("login")
            }) {
                // Icono para el botón de logout
                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
            }
        }
    )
}