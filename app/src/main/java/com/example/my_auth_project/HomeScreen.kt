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
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: ProductViewModel, onNavigateToAdd: () -> Unit, onLogout: () -> Unit) {
    val scope = rememberCoroutineScope()
    val repository = remember { AuthRepository() }

    LaunchedEffect(Unit) {
        viewModel.fetchCategorias()
        viewModel.fetchProductos()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) { Icon(Icons.Default.Add, "Añadir") }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Mis Productos", style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = { scope.launch { repository.logout(); onLogout() } }) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, "Logout")
                }
            }

            // Chips de Filtro
            LazyRow {
                item {
                    FilterChip(selected = true, onClick = { viewModel.fetchProductos() }, label = { Text("Todos") })
                }
                // Añadimos el tipo Categoria aquí:
                items(viewModel.categorias) { cat: Categoria ->
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.fetchProductos(cat.id) },
                        label = { Text(cat.nombre) }
                    )
                }
            }

            LazyColumn {
                items(viewModel.productos) { prod ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = prod.nombre, style = MaterialTheme.typography.titleLarge)
                                Text(text = "${prod.precio}€", color = MaterialTheme.colorScheme.secondary)
                            }

                            // Mostrar Categoría (JOIN)
                            Text(
                                text = "Categoría: ${prod.Categorias?.nombre ?: "Sin categoría"}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            // Mostrar Usuario Creador (JOIN con la tabla profiles)
                            Text(
                                text = "Creado por: ${prod.profiles?.nombre ?: "Cargando..."}",
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
