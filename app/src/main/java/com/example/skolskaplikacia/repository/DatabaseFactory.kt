package com.example.skolskaplikacia.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skolskaplikacia.viewModels.DochadzkaViewModel
import com.example.skolskaplikacia.viewModels.LoginViewModel
import com.example.skolskaplikacia.viewModels.MenuViewModel
import com.example.skolskaplikacia.viewModels.RozsireneZnamkyViewModel
import com.example.skolskaplikacia.viewModels.RozvrhViewModel
import com.example.skolskaplikacia.viewModels.SpravyViewModel
import com.example.skolskaplikacia.viewModels.ZnamkyViewModel

class DatabaseFactory(
    private val osobaRepository: OsobaRepository,
    private val rozvrhRepository: RozvrhRepository,
    private val detiRepository: DetiRepository,
    private val spravyRepository: SpravyRepository,
    private val znamkyRepository: ZnamkyRepository,
    private val dochadzkaRepository: DochadzkaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository) as T
            }
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository, dochadzkaRepository) as T
            }
            modelClass.isAssignableFrom(SpravyViewModel::class.java) -> {
                SpravyViewModel(spravyRepository) as T
            }
            modelClass.isAssignableFrom(ZnamkyViewModel::class.java) -> {
                val menuViewModel = MenuViewModel(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository, dochadzkaRepository)
                ZnamkyViewModel(znamkyRepository, menuViewModel) as T
            }
            modelClass.isAssignableFrom(RozsireneZnamkyViewModel::class.java) -> {
                val menuViewModel = MenuViewModel(osobaRepository, rozvrhRepository, detiRepository, spravyRepository, znamkyRepository, dochadzkaRepository)
                RozsireneZnamkyViewModel(znamkyRepository, menuViewModel) as T
            }
            modelClass.isAssignableFrom(RozvrhViewModel::class.java) -> {
                RozvrhViewModel(rozvrhRepository) as T
            }
            modelClass.isAssignableFrom(DochadzkaViewModel::class.java) -> {
                DochadzkaViewModel(dochadzkaRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
