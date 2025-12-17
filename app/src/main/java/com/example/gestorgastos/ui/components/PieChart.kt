package com.example.gestorgastos.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gestorgastos.domain.model.CategoryReportItem
import com.example.gestorgastos.ui.screens.reporteria.ReporteriaScreen

@Composable
fun PieChart(
    data: List<CategoryReportItem>,
    radiusOuter: Dp = 100.dp,
    chartBarWidth: Dp = 20.dp,
    totalAmount: Double,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    val totalValue = data.sumOf { it.totalAmount }

    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(key1 = data) {
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = 800))
    }

    Box(
        modifier = Modifier.size(radiusOuter * 2f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            val radius = width / 2f
            val strokeWidth = chartBarWidth.toPx()

            var startAngle = -90f

            data.forEach { item ->
                val sweepAngle = (item.percentage * 360f) * animationProgress.value

                drawArc(
                    color = item.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )

                startAngle += sweepAngle
            }
        }

        Text(
            text = "$${String.format("%.0f", totalAmount)}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}