package com.example.my_auth_project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my_auth_project.viewmodel.ProductViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: ProductViewModel, onNavigateToAdd: () -> Unit, onLogout: () -> Unit) {
    val scope = rememberCoroutineScope()
    // Instanciamos el repositorio para el logout
    val repository = remember { AuthRepository() }
    val user = remember { SupabaseManager.client.auth.currentUserOrNull() }

    // Corregido: Ahora la llave se cierra correctamente después de calcular el nombre
    val nombreUsuario = remember(user) {
        val metadata = user?.userMetadata
        val rawName = metadata?.get("nombre")
            ?: metadata?.get("display_name")
            ?: metadata?.get("full_name")

        rawName?.toString()?.trim('"') ?: "Usuario"
    } // <--- ESTA LLAVE FALTABA

    LaunchedEffect(Unit) {
        viewModel.fetchCategorias()
        viewModel.fetchProductos()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Mis Productos", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = {
                    scope.launch {
                        repository.logout()
                        onLogout()
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, "Logout")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chips de Filtro
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = true,
                        onClick = { viewModel.fetchProductos() },
                        label = { Text("Todos") }
                    )
                }
                items(viewModel.categorias) { cat ->
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.fetchProductos(cat.id) },
                        label = { Text(cat.nombre) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewModel.productos) { prod ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = prod.nombre, style = MaterialTheme.typography.titleLarge)
                                Text(text = "${prod.precio}€", color = MaterialTheme.colorScheme.secondary)
                            }

                            // Mostrar Categoría (Asegúrate que en el Modelo sea 'categorias')
                            Text(
                                text = "Categoría: ${prod.categorias?.nombre ?: "Sin categoria"}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            // Mostrar Usuario de la sesión
                            Text(
                                text = "Creado por: $nombreUsuario",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}