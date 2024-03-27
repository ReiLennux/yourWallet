package com.example.simplelogin.data.model

data class Card(
    val cardNumber: String = "",
    val expirationDate: String = "",
    val cvv: String = "",
    val balance: Double = 0.0, // Saldo de la tarjeta de crédito
    val address: String = "",
    val bank: String = ""      // Banco emisor de la tarjeta de crédito
)