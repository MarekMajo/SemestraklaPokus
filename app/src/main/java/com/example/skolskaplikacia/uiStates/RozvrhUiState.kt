package com.example.skolskaplikacia.uiStates

data class RozvrhUiState (
    val rozvrhZoznam: List<BlokRozvrhu>,
    val selectUser: Int
)

data class BlokRozvrhu(
    val den: Int,
    val blok: Int,
    val texty: List<String>
)

data class BlokDna(
    val id: Int,
    val den: String
)

val blokyDni = listOf(
    BlokDna(1, "Pondelok"),
    BlokDna(2, "Utorok"),
    BlokDna(3, "Streda"),
    BlokDna(4, "Štvrtok"),
    BlokDna(5, "Piatok")
)