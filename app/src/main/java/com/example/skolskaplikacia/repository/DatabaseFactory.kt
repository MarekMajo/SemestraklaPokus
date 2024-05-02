package com.example.skolskaplikacia.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skolskaplikacia.viewModels.LoginViewModel
import com.example.skolskaplikacia.viewModels.MenuViewModel
import com.example.skolskaplikacia.viewModels.RozvrhViewModel
import com.example.skolskaplikacia.viewModels.SpravyViewModel
import com.example.skolskaplikacia.viewModels.ZnamkyViewModel

class DatabaseFactory(
    private val osobaRepository: OsobaRepository,
    private val rozvrhRepository: RozvrhRepository,
    private val detiRepository: DetiRepository,
    private val spravyRepository: SpravyRepository,
    private val znamkyRepository: ZnamkyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository) as T
            }
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository) as T
            }
            modelClass.isAssignableFrom(SpravyViewModel::class.java) -> {
                SpravyViewModel(spravyRepository) as T
            }
            modelClass.isAssignableFrom(ZnamkyViewModel::class.java) -> {
                ZnamkyViewModel(osobaRepository, detiRepository, znamkyRepository) as T
            }
            modelClass.isAssignableFrom(RozvrhViewModel::class.java) -> {
                RozvrhViewModel(rozvrhRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
