package com.example.inventa2.ui

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.inventa2.R // Importante para poder leer la carpeta drawable

@Composable
fun LoginScreen(onLoginClick: () -> Unit) {
    val contexto = LocalContext.current

    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // Función que invoca el sensor de huella del sistema operativo
    fun autenticarConHuella() {
        val activity = contexto as? FragmentActivity
        if (activity == null) {
            Toast.makeText(contexto, "Biometría no soportada en esta pantalla", Toast.LENGTH_SHORT).show()
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(contexto, "Autenticación cancelada", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(contexto, "¡Huella aceptada!", Toast.LENGTH_SHORT).show()
                    onLoginClick() // Navega al Dashboard
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(contexto, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso a Inventa2")
            .setSubtitle("Toca el sensor de huella para entrar a la bodega")
            .setNegativeButtonText("Usar usuario y contraseña")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.logovaria),
            contentDescription = "Logo de INVENTA2",
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(24.dp))
                .padding(bottom = 32.dp)
        )

        // Campo de Usuario
        OutlinedTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = { Text("Usuario") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            singleLine = true
        )

        // Campo de Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            singleLine = true
        )

        // Botón Clásico (Mantiene tu lógica de acceso manual)
        Button(
            onClick = {
                val userLimpio = usuario.trim().lowercase()

                if ((userLimpio == "jonathan" || userLimpio == "alfredo") && contrasena == "UACM123") {
                    onLoginClick()
                } else {
                    Toast.makeText(contexto, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Iniciar Sesión", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NUEVO BOTÓN PARA HUELLA DACTILAR
        OutlinedButton(
            onClick = { autenticarConHuella() },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Icon(Icons.Default.Lock, contentDescription = "Huella")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Entrar con Huella Dactilar", fontSize = 16.sp)
        }
    }
}