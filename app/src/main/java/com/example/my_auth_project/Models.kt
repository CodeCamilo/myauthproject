package com.example.my_auth_project

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String? = null,
    val nombre: String? = null,
    val email: String? = null,
    val rol: String? = null
)

@Serializable
data class Categoria(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null
)

@Serializable
data class Producto(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String? = null,
    val precio: Double,
    val categoria_id: Int,
    val user_id: String,
    // Relaciones (JOINs)
    // El nombre de la variable debe ser IGUAL al nombre de la tabla en tu imagen
    val categorias: Categoria? = null,
    val profiles: Profile? = null
)