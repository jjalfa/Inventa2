package com.example.inventa2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventa2.data.ProductoRepository
import com.example.inventa2.ui.DashboardScreen
import com.example.inventa2.ui.EscanerScreen
import com.example.inventa2.ui.InventarioScreen
import com.example.inventa2.ui.LoginScreen
import com.example.inventa2.ui.RegistroScreen
import com.example.inventa2.ui.theme.Inventa2Theme
import com.example.inventa2.viewmodel.InventarioViewModel
import com.example.inventa2.viewmodel.InventarioViewModelFactory
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el repositorio que se conecta a Firestore
        val repository = ProductoRepository()

        enableEdgeToEdge()
        setContent {
            Inventa2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "login") {

                        composable("login") {
                            LoginScreen(onLoginClick = { navController.navigate("dashboard") })
                        }

                        composable("dashboard") {
                            // Inyectamos el repositorio en la fábrica del ViewModel
                            val viewModel: InventarioViewModel = viewModel(factory = InventarioViewModelFactory(repository))
                            DashboardScreen(
                                viewModel = viewModel,
                                onVerInventarioClick = { navController.navigate("inventario") },
                                onEscanearClick = { navController.navigate("escaner") },
                                onRegistroClick = { navController.navigate("registro/vacio") },
                                onSalirClick = { navController.popBackStack() }
                            )
                        }

                        composable("inventario") {
                            val viewModel: InventarioViewModel = viewModel(factory = InventarioViewModelFactory(repository))
                            InventarioScreen(
                                viewModel = viewModel,
                                onAgregarClick = { navController.navigate("registro/vacio") }
                            )
                        }

                        composable("escaner") {
                            EscanerScreen(
                                onCodigoAceptado = { codigo ->
                                    navController.navigate("registro/$codigo") {
                                        popUpTo("dashboard")
                                    }
                                }
                            )
                        }

                        composable("registro/{codigo}") { backStackEntry ->
                            val codigoViajero = backStackEntry.arguments?.getString("codigo") ?: "vacio"

                            val viewModel: InventarioViewModel = viewModel(factory = InventarioViewModelFactory(repository))
                            RegistroScreen(
                                viewModel = viewModel,
                                codigoInicial = codigoViajero,
                                onRegistroExitoso = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}