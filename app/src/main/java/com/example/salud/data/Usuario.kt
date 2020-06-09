package com.example.salud.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val full_name: String,
    val email: String,
    val password: String,
    val profile_picture: String
){
}