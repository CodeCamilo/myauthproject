package com.example.my_auth_project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my_auth_project.viewmodel.ProductViewModel
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect

@Composable
fun AddProductScreen(
    viewModel: ProductViewModel,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var selectedCatId by remember { mutableStateOf<Int?>(null) }

    // CARGA INICIAL: Asegura que las categorías existan al abrir la pantalla
    LaunchedEffect(Unit) {
        if (viewModel.categorias.isEmpty()) {
            viewModel.fetchCategorias()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Nuevo Producto", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = precio,
            onValueChange = {
                // Solo permite números y un punto decimal
                if (it.isEmpty() || it.toDoubleOrNull() != null) {
                    precio = it
                }
            },
            label = { Text("Precio (€)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text(
            "Selecciona Categoría:",
            modifier = Modifier.align(Alignment.Start).padding(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )

        // Selector de categorías
    /*
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.categorias) { cat ->
                FilterChip(
                    selected = selectedCatId == cat.id,
                    onClick = {
                        // Si ya está seleccionada, la deselecciona (opcional)
                        selectedCatId = if (selectedCatId == cat.id) null else cat.id
                    },
                    label = { Text(cat.nombre) },
                    leadingIcon = if (selectedCatId == cat.id) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                    } else null
                )
            }
        }

    */

        if (viewModel.categorias.isEmpty()) {
            Text("Cargando categorías...", style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

        Button(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            // El botón solo se activa si todo está completo
            enabled = nombre.isNotBlank() && precio.isNotBlank() && selectedCatId != null,
            onClick = {
                val precioDouble = precio.toDoubleOrNull() ?: 0.0
                viewModel.crearProducto(nombre, precioDouble, selectedCatId!!)
                onBack()
            }
        ) {
            Text("Guardar Producto")
        }

        TextButton(onClick = onBack, modifier = Modifier.padding(top = 8.dp)) {
            Text("Cancelar")
        }
    }
}

