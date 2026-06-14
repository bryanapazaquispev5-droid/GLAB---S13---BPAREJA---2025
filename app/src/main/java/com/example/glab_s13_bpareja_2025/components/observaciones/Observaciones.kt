package com.example.glab_s13_bpareja_2025.components.observaciones

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ObservationScreen() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ObservationItem(
            label = "El Desafío",
            text = "Coordinar múltiples animaciones de estado en el prototipo de videojuego, asegurando que el escalado de 'ATACAR' y las transiciones de vida del enemigo se sintieran fluidas y responsivas."
        )
        ObservationItem(
            label = "Asistencia de IA",
            text = "Utilizada para patrones arquitectónicos de UI, estilos modernos de Material3 y optimización del manejo de estados de animación."
        )
    }
}

@Composable
fun ObservationItem(label: String, text: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Justify
        )
    }
}
