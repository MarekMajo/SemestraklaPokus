package com.example.skolskaplikacia.uiStates

import com.example.skolskaplikacia.network.dochadzkaDen

data class DochadzkaUiState (
    val selectUser: Int,
    val dochadzka: List<dochadzkaDen>,
    val celkoveChybanie: Int,
    val pocetOspravedlnenych: Int,
    val pocetNeospravedlnenych: Int
)