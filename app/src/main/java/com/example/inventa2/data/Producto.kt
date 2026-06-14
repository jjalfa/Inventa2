package com.example.inventa2.data



// Hemos eliminado los imports de @Entity y @PrimaryKey de Room

data class Producto(
    var id: String = "",          // Ahora es String para soportar los IDs de Firestore
    var nombre: String = "",      // Ej. "Monitor Dell 24 pulgadas"
    var categoria: String = "",   // Ej. "Monitores", "Computadoras", "Accesorios"
    var stock: Int = 0,           // Cantidad disponible en bodega
    var precio: Double = 0.0,     // Costo del equipo
    var codigoBarras: String = "" // El código que leerá la cámara
)