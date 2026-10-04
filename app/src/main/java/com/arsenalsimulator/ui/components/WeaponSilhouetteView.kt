package com.arsenalsimulator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import com.arsenalsimulator.data.model.Category
import com.arsenalsimulator.data.model.Weapon
import kotlinx.coroutines.delay

@Composable
fun WeaponSilhouetteView(weapon: Weapon?, shootSignal: Int = 0) {
    var rotation by remember { mutableFloatStateOf(0f) }
    var scale by remember { mutableFloatStateOf(1f) }
    var flash by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(shootSignal) {
        if (shootSignal > 0) {
            flash = 1f
            while (flash > 0f) {
                delay(30)
                flash = (flash - 0.1f).coerceAtLeast(0f)
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, drag ->
                    change.consume()
                    rotation += drag.x * 0.4f
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.5f, 2.5f)
                }
            }
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        rotate(rotation, pivot = Offset(cx, cy)) {
            drawSilhouette(weapon, cx, cy, scale)
        }
        if (flash > 0f) {
            drawCircle(
                color = Color(0xFFFFCC66).copy(alpha = flash * 0.7f),
                radius = 60f * flash * scale,
                center = Offset(cx + 220f * scale, cy - 40f * scale)
            )
        }
    }
}

private fun DrawScope.drawSilhouette(weapon: Weapon?, cx: Float, cy: Float, scale: Float) {
    val body = Color(0xFF3A3F44)
    val accent = Color(0xFF6B7280)

    when (weapon?.category) {
        Category.PISTOL, Category.REVOLVER, Category.SMG -> {
            drawRect(body, Offset(cx - 120f * scale, cy - 30f * scale), Size(200f * scale, 45f * scale))
            drawRect(accent, Offset(cx - 180f * scale, cy - 20f * scale), Size(80f * scale, 20f * scale))
            drawRect(body, Offset(cx - 40f * scale, cy + 10f * scale), Size(45f * scale, 90f * scale))
        }
        Category.RIFLE, Category.ASSAULT_RIFLE, Category.SNIPER, Category.SHOTGUN -> {
            drawRect(accent, Offset(cx - 300f * scale, cy - 15f * scale), Size(340f * scale, 22f * scale))
            drawRect(body, Offset(cx - 40f * scale, cy - 30f * scale), Size(260f * scale, 50f * scale))
            drawRect(body, Offset(cx + 200f * scale, cy - 20f * scale), Size(110f * scale, 40f * scale))
            drawRect(accent, Offset(cx - 10f * scale, cy + 10f * scale), Size(50f * scale, 90f * scale))
        }
        Category.MACHINE_GUN, Category.ARTILLERY -> {
            drawRect(body, Offset(cx - 260f * scale, cy - 40f * scale), Size(520f * scale, 80f * scale))
            drawRect(accent, Offset(cx - 400f * scale, cy - 20f * scale), Size(160f * scale, 35f * scale))
        }
        else -> {
            drawRect(body, Offset(cx - 150f * scale, cy - 25f * scale), Size(300f * scale, 50f * scale))
        }
    }
}
