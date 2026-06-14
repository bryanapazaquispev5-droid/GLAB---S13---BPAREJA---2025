package com.example.glab_s13_bpareja_2025.components.dimensiones

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.glab_s13_bpareja_2025.components.comun.AnimatedOutlinedButton

@Composable
fun SizeAndPositionAnimationScreen() {
    var isExpanded by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (isExpanded) 180.dp else 100.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "Size"
    )
    val offset by animateDpAsState(
        targetValue = if (isExpanded) 50.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "Offset"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AnimatedOutlinedButton(
            onClick = { isExpanded = !isExpanded }
        ) {
            Text(if (isExpanded) "Restablecer Diseño" else "Expandir Diseño")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.height(240.dp).fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .offset(x = offset, y = offset)
                    .size(size)
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            )
        }
    }
}
