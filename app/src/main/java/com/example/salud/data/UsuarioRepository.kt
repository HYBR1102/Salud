package com.example.salud.data

class UsuarioRepository(val usuarioDao: UsuarioDao) {
    suspend fun guardarUsuario(usuario: Usuario) = usuarioDao.guardarUsuario(usuario)
    fun obtenerUsuario(id: Int) = usuarioDao.obtenerUsuario(id)
    fun obtenerUsuarios() = usuarioDao.obtenerUsuarios()
    fun borrarUsuario(id: Int) = usuarioDao.borrarUsuario(id)
    fun iniciarSesionLogin(username: String, password: String): Boolean = usuarioDao.iniciarSesionUsuario(username, password)
}