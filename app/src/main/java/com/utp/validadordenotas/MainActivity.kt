package com.utp.validadordenotas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utp.validadordenotas.ui.theme.ValidadorDeNotasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ValidadorDeNotasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    // ---------- Estados ----------
    var notaTexto by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val ctx = LocalContext.current

    // ---------- Layout ----------
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Parcial #1", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Aldo Apicella")

        Spacer(Modifier.height(32.dp))
        Text("Ingrese la nota a validar:")

        OutlinedTextField(
            value = notaTexto,
            onValueChange = {
                notaTexto = it
                error = null          // limpia error al escribir
            },
            placeholder = { Text("0-100") },
            isError = error != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(24.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // ----- Validaciones -----
                if (notaTexto.isBlank()) {
                    error = "Obligatorio"
                    return@Button
                }
                val nota = notaTexto.toIntOrNull()
                if (nota == null) {
                    error = "Sólo números"
                    return@Button
                }
                if (nota !in 0..100) {
                    error = "Rango 0-100"
                    return@Button
                }

                // ----- Resultado -----
                val (letra, desc) = calificar(nota)
                Toast.makeText(
                    ctx,
                    "Tu nota es $nota: $letra ($desc)",
                    Toast.LENGTH_LONG
                ).show()
            }
        ) {
            Text("VALIDAR")
        }
    }
}

private fun calificar(n: Int): Pair<String, String> = when (n) {
    in 91..100 -> "A" to "Excelente"
    in 81..90  -> "B" to "Bueno"
    in 71..80  -> "C" to "Regular"
    in 61..70  -> "D" to "Más o menos regular"
    else       -> "F" to "No aprobado, gracias por participar"
}
