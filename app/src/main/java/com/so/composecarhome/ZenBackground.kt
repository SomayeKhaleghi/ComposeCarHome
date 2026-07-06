package com.so.composecarhome

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.animation.core.*


@Composable
fun ZenAnimatedBackground() {
    // State for leaves
    val leafCount = 12
    val leaves = remember {
        List(leafCount) {
            Leaf(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 15f + 8f,
                speed = Random.nextFloat() * 0.004f + 0.003f,
                drift = Random.nextFloat() * 0.02f - 0.01f,
                rotationSpeed = Random.nextFloat() * 0.03f - 0.015f,
                alpha = Random.nextFloat() * 0.5f + 0.3f
            )
        }
    }

    // State for ripples
    val ripples = remember { mutableStateListOf<Ripple>() }
    var rippleTimer by remember { mutableFloatStateOf(0f) }

    // Infinite animation loop
    val infiniteTransition = rememberInfiniteTransition(label = "zen_loop")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing)
        )
    )
    // Spawn new ripples periodically
    LaunchedEffect(time) {
        rippleTimer += 0.02f
        if (rippleTimer >= 1.5f) { // New ripple every 1.5 seconds
            rippleTimer = 0f
            ripples.add(
                Ripple(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    startTime = time
                )
            )
            // Keep only latest 15 ripples
            if (ripples.size > 15) ripples.removeAt(0)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. DEEP ZEN GRADIENT (Sunset / Twilight)
        val brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A1A2E), // Deep dark blue
                Color(0xFF16213E), // Navy
                Color(0xFF0F3460), // Deep Indigo
                Color(0xFF533483)  // Soft purple at bottom
            )
        )
        drawRect(brush = brush)

        // 2. DRAW Ripples (Stone drops on water)
        ripples.forEach {  ripple: Ripple ->
            val age = (time - ripple.startTime) / 3000f // 3 seconds lifetime
            if (age < 1f) {
                val radius = age * 150f // Expand up to 150dp
                val alpha = (1f - age) * 0.6f
                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = radius.coerceAtMost(width * 0.3f),
                    center = Offset(ripple.x * width, ripple.y * height),
                    style = Stroke(width = 2.dp.toPx())
                )
                // Inner glow ripple
                drawCircle(
                    color = Color(0xFF88DDFF).copy(alpha = alpha * 0.3f),
                    radius = radius.coerceAtMost(width * 0.3f) * 0.8f,
                    center = Offset(ripple.x * width, ripple.y * height),
                    style = Stroke(width = 6.dp.toPx())
                )
            }
        }

        // 3. DRAW Floating Leaves (Wind effect)
        leaves.forEach { leaf: Leaf ->
            val offsetX = (leaf.x + time * leaf.speed * 0.5f) % 1f
            val offsetY = (leaf.y + sin(time * leaf.drift * 2f) * 0.1f) % 1f
            val angle = time * leaf.rotationSpeed

            rotate(degrees = angle, pivot = Offset(offsetX * width, offsetY * height)) {
                drawOval(
                    color = Color.White.copy(alpha = leaf.alpha),
                    topLeft = Offset(
                        offsetX * width - leaf.size / 2,
                        offsetY * height - leaf.size / 4
                    ),
                    size = androidx.compose.ui.geometry.Size(leaf.size, leaf.size * 0.6f),
                    style = Stroke(width = 1.5f)
                )
                // Fill the leaf with a soft autumn color (golden/orange)
                drawOval(
                    color = Color(0x66F5A623), // Semi-transparent orange
                    topLeft = Offset(
                        offsetX * width - leaf.size / 2,
                        offsetY * height - leaf.size / 4
                    ),
                    size = androidx.compose.ui.geometry.Size(leaf.size, leaf.size * 0.6f)
                )
            }
        }
    }
}

// Data classes for animation
private data class Leaf(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val drift: Float,
    val rotationSpeed: Float,
    val alpha: Float
)

private data class Ripple(
    val x: Float,
    val y: Float,
    val startTime: Float
)