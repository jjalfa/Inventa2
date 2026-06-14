package com.example.inventa2.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProductoRepository {
    // Obtenemos la instancia de Firestore
    private val db = FirebaseFirestore.getInstance()
    // Apuntamos a la "colección" (equivalente a tu antigua tabla_productos)
    private val coleccion = db.collection("productos")

    /**
     * Reemplaza a obtenerTodos() de Room.
     * callbackFlow crea un flujo continuo de datos. addSnapshotListener "escucha"
     * la base de datos en tiempo real. Si alguien actualiza el stock desde otro
     * celular, tu app se actualizará instantáneamente.
     */
    fun obtenerProductos(): Flow<List<Producto>> = callbackFlow {
        val listener = coleccion.orderBy("nombre").addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            // Mapeamos los documentos de Firebase a nuestra clase Producto
            val lista = snapshot?.documents?.mapNotNull { doc ->
                val producto = doc.toObject(Producto::class.java)
                producto?.id = doc.id // Le inyectamos el ID alfanumérico generado
                producto
            } ?: emptyList()

            trySend(lista)
        }
        // Cuando la vista deje de observar, cerramos el listener para ahorrar batería
        awaitClose { listener.remove() }
    }

    /**
     * Reemplaza a buscarPorCodigo() de Room.
     * Busca un documento donde el campo 'codigoBarras' coincida.
     */
    suspend fun buscarPorCodigo(codigo: String): Producto? {
        val snapshot = coleccion.whereEqualTo("codigoBarras", codigo).get().await()
        val doc = snapshot.documents.firstOrNull()
        val producto = doc?.toObject(Producto::class.java)
        producto?.id = doc?.id ?: ""
        return producto
    }

    /**
     * Reemplaza a insertarProducto() y actualizarProducto() de Room.
     * En Firestore, '.set()' sobrescribe si existe o crea si es nuevo.
     */
    suspend fun guardarProducto(producto: Producto) {
        if (producto.id.isEmpty()) {
            // Si no tiene ID, es un producto nuevo. Dejamos que Firestore genere el ID.
            coleccion.add(producto).await()
        } else {
            // Si ya tiene ID, actualizamos el documento existente.
            coleccion.document(producto.id).set(producto).await()
        }
    }

    /**
     * Reemplaza a eliminarProducto() de Room.
     */
    suspend fun eliminarProducto(producto: Producto) {
        if (producto.id.isNotEmpty()) {
            coleccion.document(producto.id).delete().await()
        }
    }
}