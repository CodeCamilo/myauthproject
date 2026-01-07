package com.example.my_auth_project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(onSignUpSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    val repository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Nuevo estado para el mensaje de error
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally) {
        Text("Registro", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(password, { password = it }, label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        // Mostrar el error si existe
        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        isLoading = true
                        errorMessage = null // Limpiar error previo
                        try {
                            repository.signUp(email, password, nombre)
                            onSignUpSuccess()
                        } catch (e: Exception) {
                            // Capturamos el mensaje de error de Supabase
                            errorMessage = when {
                                e.message?.contains("weak", ignoreCase = true) == true ->
                                    "La contraseña es muy débil (mínimo 6 caracteres)."
                                e.message?.contains("already registered", ignoreCase = true) == true ->
                                    "Este correo ya está registrado."
                                else -> "Error: ${e.localizedMessage}"
                            }
                        } finally {
                            isLoading = false
                        }
                    }
                }
            ) { Text("Registrarse") }
        }

        TextButton(onClick = onBackToLogin) { Text("Volver al Login") }
    }
}