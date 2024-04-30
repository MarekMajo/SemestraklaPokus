package com.example.skolskaplikacia.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skolskaplikacia.viewModels.LoginViewModel
import com.example.skolskaplikacia.viewModels.MenuViewModel
import com.example.skolskaplikacia.viewModels.SpravyViewModel

class DatabaseFactory(
    private val osobaRepository: OsobaRepository,
    private val rozvrhRepository: RozvrhRepository,
    private val detiRepository: DetiRepository,
    private val spravyRepository: SpravyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(osobaRepository, rozvrhRepository, detiRepository, spravyRepository) as T
            }
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(osobaRepository, rozvrhRepository, detiRepository, spravyRepository) as T
            }
            modelClass.isAssignableFrom(SpravyViewModel::class.java) -> {
                SpravyViewModel(spravyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
