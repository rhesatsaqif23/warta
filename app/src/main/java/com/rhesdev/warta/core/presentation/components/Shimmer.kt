package com.rhesdev.warta.core.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.rhesdev.warta.core.presentation.theme.WartaTheme

// Animated shimmer skeleton for loading placeholders.
@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(rememberShimmerBrush()))
}

@Composable
fun rememberShimmerBrush(): Brush {
    val base = MaterialTheme.colorScheme.surfaceVariant
    val shimmerColors = listOf(
        base.copy(alpha = 0.5f),
        base.copy(alpha = 1f),
        base.copy(alpha = 0.5f)
    )
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translate, y = translate)
    )
}

@Preview(showBackground = true)
@Composable
private fun ShimmerBoxPreview() {
    WartaTheme {
        ShimmerBox()
    }
}
