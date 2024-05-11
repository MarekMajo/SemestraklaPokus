package com.example.skolskaplikacia.uiStates

data class ZnamkyUiState (
    val predmetyZiaka: List<VysledokPredmetu>,
    val selectUser: Int
)

data class Znamka(val znamka: Int, val vaha: Double, val podpisane: Int)
data class VysledokPredmetu(val predmet: String, val predmetID: Int, val znamky: List<Znamka>, val priemer: String)