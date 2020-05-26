package com.example.salud.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.salud.data.UsuarioRepository

class UsuarioViewModelFactory(private val usuarioRepository: UsuarioRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UsuarioViewModel(usuarioRepository) as T
    }
}