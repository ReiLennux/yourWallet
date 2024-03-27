package com.example.simplelogin

import CreditCardScreen
import LoginScreen
import RegisterCardScreen
import RegisterScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.SimpleLoginTheme
import com.example.simplelogin.ui.screen.*

import com.example.simplelogin.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels() // Obtén una instancia de AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleLoginTheme {
                // Configura el NavHost para la navegación
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "login") {
                        // Define las pantallas y sus rutas
                        composable("register") {
                            RegisterScreen(navController = navController, authViewModel = authViewModel)
                        }
                        composable("login") {
                            LoginScreen(navController = navController, authViewModel = authViewModel)
                        }
                        composable("CreditCardScreen") {
                            CreditCardScreen(navController = navController, authViewModel = authViewModel)
                        }
                        composable("RegisterCardScreen") {
                            RegisterCardScreen(navController = navController, authViewModel = authViewModel)
                        }
                    }
                }
            }
        }
    }
}

