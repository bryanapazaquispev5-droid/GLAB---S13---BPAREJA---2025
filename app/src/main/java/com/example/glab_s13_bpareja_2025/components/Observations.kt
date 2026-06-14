package com.example.glab_s13_bpareja_2025.components

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
            label = "The Challenge",
            text = "Coordinating multiple state animations in the videogame prototype, ensuring that 'STRIKE' scaling and enemy health transitions felt responsive."
        )
        ObservationItem(
            label = "AI Assistance",
            text = "Used for UI architectural patterns, modern Material3 styling, and optimizing animation state management."
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
