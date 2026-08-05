package com.example.easywallet

data class Movement(
    val type: String,
    val date: String,
    val amount: String,
    val status: String,
    val isPositive: Boolean
)