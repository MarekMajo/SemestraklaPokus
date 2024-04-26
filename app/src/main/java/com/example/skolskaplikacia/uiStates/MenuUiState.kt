package com.example.skolskaplikacia.uiStates

data class MenuUiState(
    val meno: String?,
    val priezvisko: String?,
    val isDropdownVisible: Boolean = false,
    val blokyVDni: List<BlokTextu>
)

data class BlokCasu(
    val id: Int,
    val cas: List<String>
)
data class BlokTextu(
    val id: Int,
    val texty: List<String>
)
val blokyCasov = listOf(
    BlokCasu(1, listOf("7:45", "8:30")),
    BlokCasu(2, listOf("8:40", "9:25")),
    BlokCasu(3, listOf("9:40", "10:25")),
    BlokCasu(4, listOf("10:45", "11:30")),
    BlokCasu(5, listOf("11:40", "12:25")),
    BlokCasu(6, listOf("12:30", "13:15")),
    BlokCasu(7, listOf("13:30", "14:15"))
)