package com.example.janaushadhifinder

data class Medicine(
    val brandName: String = "",
    val genericName: String = "",
    val brandedPrice: Double = 0.0,
    val genericPrice: Double = 0.0,
    val category: String = "",
    val isAvailable: Boolean = true
)
