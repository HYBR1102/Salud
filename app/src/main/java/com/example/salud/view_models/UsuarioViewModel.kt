package com.example.salud.view_models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salud.data.Usuario
import com.example.salud.data.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(val usuarioRepository: UsuarioRepository): ViewModel() {
//    val usuario = usuarioRepository.obtenerUsuario(id)
//    val usuarios = usuarioRepository.obtenerUsuarios()
    fun guardarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            usuarioRepository.guardarUsuario(usuario)
        }
    }
}