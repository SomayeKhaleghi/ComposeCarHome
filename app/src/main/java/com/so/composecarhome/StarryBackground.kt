package com.so.composecarhome

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.animation.core.*
import kotlin.math.cos



@Composable
fun StarryAnimatedBackground() {
    // --- FIX 1: Fewer, smaller stars (30 stars, max size 2.5dp) ---
    val stars = remember {
        List(30) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 2f + 0.5f, // 0.5dp to 2.5dp (tiny!)
                speed = Random.nextFloat() * 0.5f + 0.2f, // Slow twinkle speed
                phase = Random.nextFloat() * 10f
            )
        }
    }

    // Shooting star state
    var shootingStarX by remember { mutableFloatStateOf(-0.2f) }
    var shootingStarY by remember { mutableFloatStateOf(0.2f) }
    var shootingStarAlpha by remember { mutableFloatStateOf(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "star_loop")

    // --- FIX 2: Slower time progression (20 seconds loop) ---
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f, // Increased loop duration
        animationSpec = infiniteRepeatable(tween(40000, easing = LinearEasing)) // 40 seconds total loop
    )

    // Shooting star trigger (every 15 seconds)
    LaunchedEffect(time) {
        val cycle = (time % 3000f) / 3000f // Reset every 15 seconds
        if (cycle < 0.08f) { // Only visible for 8% of the cycle
            shootingStarAlpha = 1f
            // Move smoothly across the screen
            val progress = cycle / 0.08f
            shootingStarX = -0.2f + progress * 1.4f
            shootingStarY = 0.2f + progress * 0.6f
        } else {
            shootingStarAlpha = 0f
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. Deep, calm night gradient (no harsh colors)
        val brush = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0A0A1A), // Almost pure black
                Color(0xFF141430), // Deep navy
                Color(0xFF1A1A3E), // Soft indigo
                Color(0xFF0F172A)  // Dark base
            )
        )
        drawRect(brush = brush)

        // --- FIX 3: Gentle, slow breathing stars (NOT strobe) ---
        stars.forEach { star ->
            // The magic fix: multiply time by 0.01 to make it 100x slower
            val brightness = (sin(time * 0.01f * star.speed + star.phase) * 0.5f + 0.5f) * 0.6f + 0.2f
            val alpha = brightness

            // Almost no drifting (keeps them calm)
            val driftX = sin(time * 0.0005f + star.x * 10f) * 0.001f
            val driftY = cos(time * 0.0005f + star.y * 10f) * 0.001f

            val xPos = (star.x + driftX) * width
            val yPos = (star.y + driftY) * height

            // Tiny soft glow (very subtle)
            drawCircle(
                color = Color.White.copy(alpha = alpha * 0.2f),
                radius = star.size * 2f,
                center = Offset(xPos, yPos)
            )
            // Tiny bright core
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = star.size,
                center = Offset(xPos, yPos)
            )
        }

        // 2. Shooting Star (Preserved exactly as you liked it)
        if (shootingStarAlpha > 0.05f) {
            val startX = shootingStarX * width
            val startY = shootingStarY * height
            val endX = (shootingStarX + 0.3f) * width
            val endY = (shootingStarY + 0.15f) * height

            // Bright head
            drawCircle(
                color = Color.White.copy(alpha = shootingStarAlpha),
                radius = 5f,
                center = Offset(startX, startY)
            )
            // Fading tail
            drawLine(
                color = Color.White.copy(alpha = shootingStarAlpha * 0.5f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 1.5f
            )
            // Outer glow tail
            drawLine(
                color = Color(0xFF88DDFF).copy(alpha = shootingStarAlpha * 0.2f),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                strokeWidth = 6f
            )
        }
    }
}

private data class Star(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float,
    val phase: Float
)