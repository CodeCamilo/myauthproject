package com.example.my_auth_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.my_auth_project.viewmodel.ProductViewModel
import io.github.jan.supabase.auth.auth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Se recomienda usar un tema de Material3 para que los componentes se vean bien
            MaterialTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    // Inicializamos el ViewModel aquí para que persista durante la sesión
    val productViewModel: ProductViewModel = viewModel()

    // Verificamos si ya existe una sesión activa en Supabase
    val initialScreen = if (SupabaseManager.client.auth.currentSessionOrNull() != null) "home" else "login"
    var screen by remember { mutableStateOf(initialScreen) }

    when (screen) {
        "login" -> LoginScreen(
            onLoginSuccess = { screen = "home" },
            onGoToSignUp = { screen = "signup" }
        )
        "signup" -> SignUpScreen(
            onSignUpSuccess = { screen = "login" },
            onBackToLogin = { screen = "login" } // Añadido para que coincida con tu SignUpScreen
        )
        "home" -> HomeScreen(
            viewModel = productViewModel,
            onNavigateToAdd = {
                // Aquí podrías cambiar a una pantalla de "Agregar Producto"
                // Por ahora, podrías imprimir un log o navegar si tuvieras la pantalla
                println("Navegar a agregar producto")
            },
            onLogout = { screen = "login" }
        )
        "add_product" -> AddProductScreen(
            viewModel = productViewModel,
            onBack = { screen = "home" }
        )
    }
}
