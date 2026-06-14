package com.example.inventa2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.inventa2.data.Producto
import com.example.inventa2.data.ProductoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InventarioViewModel(private val repository: ProductoRepository) : ViewModel() {

    // La UI observará este Flow reactivo conectado a Firestore a través del repositorio
    val productos: Flow<List<Producto>> = repository.obtenerProductos()

    // NUEVO: La búsqueda a internet toma tiempo. Usamos una corrutina y un callback (onResult)
    // para devolver el producto cuando Firestore responda, sin congelar la interfaz.
    fun buscarPorCodigoAsync(codigo: String, onResult: (Producto?) -> Unit) {
        viewModelScope.launch {
            val producto = repository.buscarPorCodigo(codigo)
            onResult(producto)
        }
    }

    fun guardarProducto(nombre: String, categoria: String, stock: Int, precio: Double, codigoBarras: String, esEntrada: Boolean) {
        viewModelScope.launch {
            val productoExistente = if (codigoBarras.isNotBlank()) {
                repository.buscarPorCodigo(codigoBarras)
            } else {
                null
            }

            if (productoExistente != null) {
                val nuevoStock = if (esEntrada) {
                    productoExistente.stock + stock
                } else {
                    val resta = productoExistente.stock - stock
                    if (resta < 0) 0 else resta
                }

                val productoActualizado = productoExistente.copy(
                    stock = nuevoStock,
                    precio = precio
                )
                // Firestore maneja inserción y actualización con la misma función .set()
                repository.guardarProducto(productoActualizado)
            } else {
                val stockInicial = if (esEntrada) stock else 0

                val nuevoProducto = Producto(
                    // Al dejar el ID vacío (""), Firestore le generará un ID alfanumérico único
                    nombre = nombre,
                    categoria = categoria,
                    stock = stockInicial,
                    precio = precio,
                    codigoBarras = codigoBarras
                )
                repository.guardarProducto(nuevoProducto)
            }
        }
    }

    fun eliminarProducto(producto: Producto) {
        viewModelScope.launch {
            repository.eliminarProducto(producto)
        }
    }
}

// ACTUALIZADO: El Factory ahora inyecta ProductoRepository en lugar de ProductoDao
class InventarioViewModelFactory(private val repository: ProductoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventarioViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}