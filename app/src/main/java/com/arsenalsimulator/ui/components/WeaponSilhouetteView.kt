package com.arsenalsimulator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import com.arsenalsimulator.data.model.Category
import com.arsenalsimulator.data.model.Weapon
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

data class Box3D(
    val cx: Float, val cy: Float, val cz: Float,
    val sx: Float, val sy: Float, val sz: Float,
    val color: Color
)

@Composable
fun WeaponSilhouetteView(weapon: Weapon?, shootSignal: Int = 0) {
    var rotation by remember { mutableFloatStateOf(0.5f) }
    var scale by remember { mutableFloatStateOf(110f) }
    var flash by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(shootSignal) {
        if (shootSignal > 0) {
            flash = 1f
            while (flash > 0f) {
                delay(30)
                flash = (flash - 0.08f).coerceAtLeast(0f)
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, drag ->
                    change.consume()
                    rotation += drag.x * 0.01f
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale = (scale * zoom).coerceIn(40f, 300f)
                }
            }
    ) {
        val cx = size.width / 2f
        val horizon = size.height * 0.55f

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF5AA8D8), Color(0xFFB8D8E8))
            ),
            size = Size(size.width, horizon)
        )

        drawCircle(
            color = Color(0xFFFFF2C0).copy(alpha = 0.6f),
            radius = 90f,
            center = Offset(size.width * 0.85f, horizon * 0.35f)
        )

        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF6B8E4E), Color(0xFF3E5A2A)),
                startY = horizon, endY = size.height
            ),
            topLeft = Offset(0f, horizon),
            size = Size(size.width, size.height - horizon)
        )

        drawLine(
            color = Color(0xFF2A3D1A),
            start = Offset(0f, horizon),
            end = Offset(size.width, horizon),
            strokeWidth = 3f
        )

        for (i in -6..6) {
            val xTop = cx + i * 80f
            val xBot = cx + i * 280f
            drawLine(
                color = Color(0xFF4A6B35).copy(alpha = 0.35f),
                start = Offset(xTop, horizon),
                end = Offset(xBot, size.height),
                strokeWidth = 1f
            )
        }

        val shadowY = horizon + 60f
        drawOval(
            color = Color.Black.copy(alpha = 0.35f),
            topLeft = Offset(cx - scale * 2.2f, shadowY),
            size = Size(scale * 4.4f, scale * 0.45f)
        )

        weapon?.let { w ->
            weaponBoxes(w).forEach { box ->
                drawBox(box, rotation, scale, cx, horizon + 10f)
            }
        }

        if (flash > 0f && weapon != null) {
            val tipX = scale * 1.8f
            val flashOffset = project(tipX, 0.4f, 0f, rotation, scale, cx, horizon + 10f)
            drawCircle(
                color = Color(0xFFFFAA33).copy(alpha = flash * 0.8f),
                radius = scale * 0.7f * flash,
                center = flashOffset
            )
            drawCircle(
                color = Color(0xFFFFF2A0).copy(alpha = flash),
                radius = scale * 0.3f * flash,
                center = flashOffset
            )
        }
    }
}

private fun project(
    x: Float, y: Float, z: Float,
    rotation: Float, scale: Float, cx: Float, cy: Float
): Offset {
    val cosR = cos(rotation)
    val sinR = sin(rotation)
    val xRot = x * cosR - z * sinR
    val zRot = x * sinR + z * cosR

    val screenX = (xRot - zRot) * 0.866f
    val screenY = y - (xRot + zRot) * 0.5f

    return Offset(cx + screenX * scale, cy - screenY * scale)
}

private fun DrawScope.drawBox(
    box: Box3D, rotation: Float, scale: Float, cx: Float, cy: Float
) {
    val x0 = box.cx - box.sx / 2
    val x1 = box.cx + box.sx / 2
    val y0 = box.cy - box.sy / 2
    val y1 = box.cy + box.sy / 2
    val z0 = box.cz - box.sz / 2
    val z1 = box.cz + box.sz / 2

    fun p(x: Float, y: Float, z: Float) = project(x, y, z, rotation, scale, cx, cy)

    drawPath(pathOf(listOf(p(x0,y0,z0), p(x1,y0,z0), p(x1,y0,z1), p(x0,y0,z1))),
        color = box.color.copy(alpha = 0.4f))
    drawPath(pathOf(listOf(p(x0,y0,z0), p(x1,y0,z0), p(x1,y1,z0), p(x0,y1,z0))),
        color = box.color.copy(alpha = 0.55f))
    drawPath(pathOf(listOf(p(x0,y0,z0), p(x0,y0,z1), p(x0,y1,z1), p(x0,y1,z0))),
        color = box.color.copy(alpha = 0.65f))

    drawPath(pathOf(listOf(p(x0,y1,z0), p(x1,y1,z0), p(x1,y1,z1), p(x0,y1,z1))),
        color = box.color.copy(alpha = 1f))
    drawPath(pathOf(listOf(p(x1,y0,z0), p(x1,y0,z1), p(x1,y1,z1), p(x1,y1,z0))),
        color = box.color.copy(alpha = 0.85f))
    drawPath(pathOf(listOf(p(x0,y0,z1), p(x1,y0,z1), p(x1,y1,z1), p(x0,y1,z1))),
        color = box.color.copy(alpha = 0.95f))
}

private fun pathOf(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path
    path.moveTo(points[0].x, points[0].y)
    for (i in 1 until points.size) {
        path.lineTo(points[i].x, points[i].y)
    }
    path.close()
    return path
}

private fun weaponBoxes(weapon: Weapon): List<Box3D> {
    val metal = Color(0xFF3A3F44)
    val darkMetal = Color(0xFF1F2429)
    val wood = Color(0xFF6B4A2A)
    val grip = Color(0xFF2A2A2A)

    return when (weapon.category) {
        Category.PISTOL -> listOf(
            Box3D(0f, 0.4f, 0f, 1.4f, 0.3f, 0.3f, metal),
            Box3D(0f, 0.1f, 0f, 1.2f, 0.3f, 0.32f, darkMetal),
            Box3D(-0.5f, -0.4f, 0f, 0.4f, 0.9f, 0.3f, grip),
            Box3D(0.8f, 0.4f, 0f, 0.3f, 0.15f, 0.15f, darkMetal)
        )
        Category.REVOLVER -> listOf(
            Box3D(0.6f, 0.4f, 0f, 1.4f, 0.25f, 0.25f, metal),
            Box3D(0f, 0.4f, 0f, 0.5f, 0.5f, 0.4f, darkMetal),
            Box3D(-0.5f, 0.4f, 0f, 0.6f, 0.4f, 0.32f, metal),
            Box3D(-0.7f, -0.3f, 0f, 0.4f, 1f, 0.3f, wood)
        )
        Category.SMG -> listOf(
            Box3D(0.8f, 0.4f, 0f, 1.5f, 0.2f, 0.2f, darkMetal),
            Box3D(0f, 0.3f, 0f, 2f, 0.5f, 0.35f, metal),
            Box3D(-0.2f, -0.6f, 0f, 0.35f, 1.2f, 0.3f, darkMetal),
            Box3D(-0.8f, -0.3f, 0f, 0.4f, 0.8f, 0.3f, grip),
            Box3D(-1.4f, 0.3f, 0f, 0.7f, 0.4f, 0.3f, darkMetal)
        )
        Category.RIFLE -> listOf(
            Box3D(1.5f, 0.4f, 0f, 2.5f, 0.15f, 0.15f, darkMetal),
            Box3D(0.6f, 0.4f, 0f, 1.2f, 0.3f, 0.3f, wood),
            Box3D(-0.4f, 0.4f, 0f, 1.2f, 0.4f, 0.35f, metal),
            Box3D(-0.6f, -0.4f, 0f, 0.35f, 0.8f, 0.3f, darkMetal),
            Box3D(-1.2f, -0.1f, 0f, 0.4f, 0.6f, 0.3f, wood),
            Box3D(-2.1f, 0.4f, 0f, 1.4f, 0.5f, 0.3f, wood)
        )
        Category.ASSAULT_RIFLE -> listOf(
            Box3D(1.5f, 0.4f, 0f, 2f, 0.15f, 0.15f, darkMetal),
            Box3D(0.6f, 0.4f, 0f, 1f, 0.3f, 0.3f, darkMetal),
            Box3D(-0.3f, 0.4f, 0f, 1.2f, 0.4f, 0.35f, metal),
            Box3D(-0.5f, -0.4f, 0f, 0.35f, 1f, 0.3f, darkMetal),
            Box3D(-1.1f, -0.2f, 0f, 0.4f, 0.7f, 0.3f, grip),
            Box3D(-1.9f, 0.4f, 0f, 1.2f, 0.4f, 0.3f, darkMetal),
            Box3D(0.3f, 0.75f, 0f, 0.25f, 0.25f, 0.08f, darkMetal)
        )
        Category.SNIPER -> listOf(
            Box3D(2f, 0.4f, 0f, 3f, 0.15f, 0.15f, darkMetal),
            Box3D(0f, 0.4f, 0f, 1.5f, 0.4f, 0.32f, metal),
            Box3D(0.2f, 0.85f, 0f, 1.8f, 0.3f, 0.3f, darkMetal),
            Box3D(-0.4f, 0.65f, 0f, 0.08f, 0.25f, 0.08f, darkMetal),
            Box3D(0.6f, 0.65f, 0f, 0.08f, 0.25f, 0.08f, darkMetal),
            Box3D(-0.4f, -0.2f, 0f, 0.35f, 0.6f, 0.3f, darkMetal),
            Box3D(-1.1f, -0.2f, 0f, 0.4f, 0.7f, 0.3f, grip),
            Box3D(-2f, 0.4f, 0f, 1.4f, 0.5f, 0.3f, wood)
        )
        Category.SHOTGUN -> listOf(
            Box3D(1.5f, 0.4f, 0f, 2.5f, 0.25f, 0.25f, darkMetal),
            Box3D(0.6f, 0.3f, 0f, 1f, 0.35f, 0.32f, wood),
            Box3D(-0.3f, 0.4f, 0f, 1.2f, 0.4f, 0.36f, metal),
            Box3D(-1.1f, -0.2f, 0f, 0.4f, 0.7f, 0.3f, wood),
            Box3D(-1.9f, 0.4f, 0f, 1.2f, 0.5f, 0.35f, wood)
        )
        Category.MACHINE_GUN -> listOf(
            Box3D(1.5f, 0.4f, 0f, 2f, 0.25f, 0.25f, darkMetal),
            Box3D(0f, 0.4f, 0f, 2.5f, 0.7f, 0.6f, metal),
            Box3D(-0.5f, -0.2f, 0.35f, 0.8f, 0.7f, 0.4f, Color(0xFF4A4A2A)),
            Box3D(-1.4f, -0.2f, 0f, 0.4f, 0.7f, 0.3f, grip),
            Box3D(1.2f, -0.6f, 0f, 0.12f, 0.5f, 0.12f, darkMetal),
            Box3D(1.2f, -0.6f, 0.3f, 0.12f, 0.5f, 0.12f, darkMetal)
        )
        Category.ARTILLERY -> listOf(
            Box3D(1.2f, 0.9f, 0f, 3.5f, 0.4f, 0.4f, darkMetal),
            Box3D(-0.5f, 0.6f, 0f, 1.8f, 0.8f, 0.8f, metal),
            Box3D(-0.9f, -0.3f, 0.5f, 0.7f, 0.7f, 0.2f, darkMetal),
            Box3D(-0.9f, -0.3f, -0.5f, 0.7f, 0.7f, 0.2f, darkMetal),
            Box3D(-1.8f, -0.6f, 0f, 1.2f, 0.3f, 0.8f, metal)
        )
        else -> listOf(
            Box3D(0f, 0.4f, 0f, 2.5f, 0.5f, 0.35f, metal)
        )
    }
}
