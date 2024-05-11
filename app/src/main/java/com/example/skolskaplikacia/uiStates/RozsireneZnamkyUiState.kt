package com.example.skolskaplikacia.uiStates

data class RozsireneZnamkyUiState (
    val nazovPredmetu: String,
    val predmetId: Int,
    val zoznamKategorii: List<Kategorie>,
    val priemer: String
)

data class Kategorie(
    val nazov: String,
    val vaha: Double,
    val typ: String,
    val maxBody: Int,
    val zoznamZnakom: List<jZnamka>,
    val priemer: String
)

data class jZnamka(
    val znamka: Int,
    val prepocet: Int?,
    val podpisane: Int
)
