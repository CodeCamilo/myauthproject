package com.example.my_auth_project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.my_auth_project.viewmodel.ProductViewModel
import androidx.compose.foundation.lazy.items

@Composable
fun AddProductScreen(
    viewModel: ProductViewModel,
    onBack: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var selectedCatId by remember { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()

    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Nuevo Producto", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre del Producto") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(precio, { precio = it }, label = { Text("Precio (€)") }, modifier = Modifier.fillMaxWidth())

        Text("Selecciona Categoría:", Modifier.padding(top = 16.dp))

        // Un selector simple de categorías
        LazyRow {
            // Especificamos explícitamente la lista y el nombre de la variable 'cat'
            items(items = viewModel.categorias) { cat ->
                FilterChip(
                    selected = selectedCatId == cat.id,
                    onClick = { selectedCatId = cat.id },
                    label = { Text(cat.nombre) },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            enabled = nombre.isNotEmpty() && precio.isNotEmpty() && selectedCatId != null,
            onClick = {
                val precioDouble = precio.toDoubleOrNull() ?: 0.0
                viewModel.crearProducto(nombre, precioDouble, selectedCatId!!)
                onBack() // Regresar a la Home
            }
        ) {
            Text("Guardar Producto")
        }
    }
}