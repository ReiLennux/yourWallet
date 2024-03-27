package com.example.simplelogin.data.model

data class User(
    val uid: String = "",
    var email: String = "",
    var fullName: String = "",
    var curp: String = "",
    var password: String = "",
    val dateOfBirth: String = "",  // Puedes cambiar el tipo de dato según prefieras (Date, LocalDate, etc.)
    val cards: List<Card>? = null  // Lista de tarjetas de crédito asociadas al usuario
)
