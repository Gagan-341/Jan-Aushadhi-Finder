package com.example.janaushadhifinder

data class Store(val name: String = "",
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val distance: Double = 0.0,
    val isOpen: Boolean = true,
    val openingHours: String = "",
    val phone: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
