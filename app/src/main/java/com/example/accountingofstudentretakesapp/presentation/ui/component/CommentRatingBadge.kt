package com.example.accountingofstudentretakesapp.presentation.ui.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CommentRatingBadge(
    label: String,
    rating: Int,
    isTotal: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = if (isTotal) "$rating/100" else "$rating/10",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = when {
                isTotal && rating >= 75 -> Color(0xFF4CAF50)
                isTotal && rating >= 50 -> Color(0xFFFFC107)
                isTotal -> Color(0xFFF44336)
                rating >= 8 -> Color(0xFF4CAF50)
                rating >= 5 -> Color(0xFFFFC107)
                else -> Color(0xFFF44336)
            }
        )
    }
}