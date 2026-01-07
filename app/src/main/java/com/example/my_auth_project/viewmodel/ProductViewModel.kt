package com.example.my_auth_project.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_auth_project.Categoria
import com.example.my_auth_project.Producto
import com.example.my_auth_project.SupabaseManager
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch


class ProductViewModel : ViewModel() {
    // Asegúrate de que este nombre coincida con tu objeto en SupabaseClient.kt
    // Si tu objeto se llama SupabaseClient, cambia SupabaseManager por SupabaseClient
    private val client = SupabaseManager.client

    val productos = mutableStateListOf<Producto>()
    val categorias = mutableStateListOf<Categoria>()

    init {
        fetchInitialData()
    }

    private fun fetchInitialData() {
        viewModelScope.launch {
            try {
                // 1. Obtenemos categorías
                val catList = client.from("categorias").select().decodeList<Categoria>()
                categorias.clear()
                categorias.addAll(catList)

                // 2. Obtenemos productos
                fetchProductos()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun fetchCategorias() {
        viewModelScope.launch {
            try {
                // Importante: Usar "Categorias" exactamente como está en tu esquema
                val res = client.from("Categorias").select().decodeList<Categoria>()

                categorias.clear()
                categorias.addAll(res)
            } catch (e: Exception) {
                println("Error cargando categorías: ${e.message}")
            }
        }
    }

    fun fetchProductos(categoriaId: Int? = null) {
        viewModelScope.launch {
            try {
                // CORRECCIÓN CLAVE:
                // Configuramos la consulta DENTRO del select
                val res = client.from("productos").select(Columns.raw("*, categorias(*), profiles(*)")) {

                    // Aquí dentro estamos construyendo la consulta antes de enviarla
                    if (categoriaId != null) {
                        filter {
                            // 'eq' existe automáticamente dentro de este bloque 'filter'
                            eq("categoria_id", categoriaId)
                        }
                    }

                }.decodeList<Producto>() // Al cerrar la llave } se envía la petición y decodificamos

                productos.clear()
                productos.addAll(res)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun crearProducto(nombre: String, precio: Double, categoriaId: Int) {
        viewModelScope.launch {
            try {
                val user = client.auth.currentUserOrNull()
                if (user != null) {
                    val nuevoProducto = Producto(
                        nombre = nombre,
                        precio = precio,
                        categoria_id = categoriaId,
                        user_id = user.id // Vinculamos al usuario autenticado
                    )

                    client.from("productos").insert(nuevoProducto)

                    // Refrescamos la lista para ver el nuevo producto
                    fetchProductos()
                }
            } catch (e: Exception) {
                println("Error al crear producto: ${e.message}")
            }
        }
    }
}