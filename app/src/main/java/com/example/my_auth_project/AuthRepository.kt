package com.example.my_auth_project

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
class AuthRepository {
    private val client = SupabaseManager.client

    suspend fun signUp(email: String, password: String, nombre: String) {
        SupabaseManager.client.auth.signUpWith(Email) {
            this.email = email
            this.password = password

            data = buildJsonObject {
                put("display_name", nombre)
                put("full_name", nombre)
                put("nombre",nombre)
            }
        }
    }

    suspend fun login(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun logout() {
        client.auth.signOut()
    }
}