package com.example.glab_s13_bpareja_2025.components

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

@Composable
fun SizeAndPositionAnimationScreen() {
    var isExpanded by remember { mutableStateOf(false) }
    val size by animateDpAsState(
        targetValue = if (isExpanded) 160.dp else 100.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "Size"
    )
    val offset by animateDpAsState(
        targetValue = if (isExpanded) 40.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "Offset"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedButton(
            onClick = { isExpanded = !isExpanded },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isExpanded) "Reset Layout" else "Expand Layout")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            Box(
                modifier = Modifier
                    .offset(x = offset, y = offset)
                    .size(size)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}
