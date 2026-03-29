package com.csd.trainlytics.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.csd.trainlytics.ui.theme.GradientEnd
import com.csd.trainlytics.ui.theme.GradientStart
import com.csd.trainlytics.ui.theme.SurfaceContainerHigh
import com.csd.trainlytics.ui.theme.SurfaceContainerHighest
import kotlin.math.roundToInt

/**
 * Horizontal ruler-style value selector with tick marks.
 *
 * @param value current value
 * @param min minimum selectable value
 * @param max maximum selectable value
 * @param step tick interval (e.g. 0.1f for 1 decimal place)
 * @param label top label text
 * @param unit unit suffix (e.g. "kg", "%", "cm")
 * @param onValueChange callback when user drags to a new value
 * @param majorTickEvery number of steps between major ticks (labeled)
 */
@Composable
fun RulerSlider(
    value: Float,
    min: Float,
    max: Float,
    step: Float = 0.1f,
    label: String,
    unit: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    majorTickEvery: Int = 10
) {
    var widthPx by remember { mutableFloatStateOf(1f) }
    // How many px per one step
    val totalSteps = ((max - min) / step).roundToInt()
    // We show a window of ~50 steps centered on the current value
    val windowSteps = 50
    val pxPerStep = widthPx / windowSteps

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceContainerHigh)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))

        // Big value display
        Text(
            text = if (step < 1f) "${"%.1f".format(value)} $unit" else "${value.roundToInt()} $unit",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(12.dp))

        // Ruler track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceContainerHighest)
                .onSizeChanged { widthPx = it.width.toFloat() }
                .pointerInput(min, max, step) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        val stepsPerPx = 1f / pxPerStep
                        val delta = -dragAmount * stepsPerPx * step
                        val newValue = (value + delta).coerceIn(min, max)
                        // Snap to step
                        val snapped = (newValue / step).roundToInt() * step
                        if (snapped != value) onValueChange(snapped.coerceIn(min, max))
                    }
                }
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                drawRulerTicks(
                    centerValue = value,
                    min = min,
                    max = max,
                    step = step,
                    windowSteps = windowSteps,
                    majorTickEvery = majorTickEvery,
                    pxPerStep = pxPerStep,
                    gradientStart = GradientStart,
                    gradientEnd = GradientEnd
                )
            }
            // Center indicator line
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(2.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
            )
        }
    }
}

private fun DrawScope.drawRulerTicks(
    centerValue: Float,
    min: Float,
    max: Float,
    step: Float,
    windowSteps: Int,
    majorTickEvery: Int,
    pxPerStep: Float,
    gradientStart: Color,
    gradientEnd: Color
) {
    val centerX = size.width / 2f
    val centerStep = (centerValue / step).roundToInt()
    val halfWindow = windowSteps / 2

    for (i in -halfWindow..halfWindow) {
        val stepIndex = centerStep + i
        val tickValue = stepIndex * step
        if (tickValue < min || tickValue > max) continue

        val x = centerX + i * pxPerStep
        val isMajor = stepIndex % majorTickEvery == 0
        val tickHeight = if (isMajor) size.height * 0.55f else size.height * 0.3f
        val tickY = (size.height - tickHeight) / 2f
        val alpha = 1f - (Math.abs(i).toFloat() / halfWindow) * 0.7f

        drawLine(
            color = if (isMajor) gradientStart.copy(alpha = alpha) else Color.Gray.copy(alpha = alpha * 0.5f),
            start = Offset(x, tickY),
            end = Offset(x, tickY + tickHeight),
            strokeWidth = if (isMajor) 2f else 1.5f
        )
    }
}
