package com.example.salud.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuario: Usuario)

    @Query("SELECT * FROM user WHERE id = :id")
    fun obtenerUsuario(id: Int): LiveData<Usuario>

    @Query("SELECT * FROM user")
    fun obtenerUsuarios(): LiveData<List<Usuario>>

    @Query("DELETE FROM user WHERE id = :id")
    fun borrarUsuario(id: Int)

    @Query("SELECT * FROM user WHERE username = :username AND password = :password")
    fun iniciarSesionUsuario(username: String, password: String): Boolean
}