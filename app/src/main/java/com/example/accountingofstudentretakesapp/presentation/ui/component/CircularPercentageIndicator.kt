package com.example.accountingofstudentretakesapp.presentation.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CircularPercentageIndicator(
    percentage: Int,
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 6.dp,
) {
    val targetPercentage = percentage.toFloat() / 100f
    val animatedProgress by animateFloatAsState(
        targetValue = targetPercentage,
        animationSpec = tween(durationMillis = 1000),
        label = "percentageAnimation"
    )
    Box(
        modifier = modifier
            .size(size)
            .drawBehind {
                val strokePx = strokeWidth.toPx()
                val radius = (size.toPx() / 2) - (strokePx / 2)
                drawCircle(
                    color = backgroundColor,
                    radius = radius,
                    style = Stroke(width = strokePx)
                )
                val sweepAngle = animatedProgress * 360f
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = strokePx)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${percentage}%",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}



