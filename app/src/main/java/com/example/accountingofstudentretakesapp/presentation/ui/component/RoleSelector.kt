package com.example.accountingofstudentretakesapp.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.accountingofstudentretakesapp.presentation.model.Role

@Composable
fun RoleSelector(
    selectedRole: Role,
    onRoleSelected: (Role) -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    val borderColor = MaterialTheme.colorScheme.outline
    val studentColor = Color(0xFF4CAF50)
    val teacherColor = Color(0xFF9C27B0)
    val adminColor = Color(0xFFFF8C00)
    val textColorSelected = Color.White
    val textColorUnselected = MaterialTheme.colorScheme.onSurface
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        val itemWidth: Dp = maxWidth / 3
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .border(2.dp, borderColor, shape)
        ) {
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .fillMaxHeight()
                    .background(
                        if (selectedRole == Role.STUDENT) studentColor else Color.Transparent
                    )
                    .clickable { onRoleSelected(Role.STUDENT) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Студент",
                    color = if (selectedRole == Role.STUDENT) textColorSelected else textColorUnselected,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .fillMaxHeight()
                    .background(
                        if (selectedRole == Role.TEACHER) teacherColor else Color.Transparent
                    )
                    .clickable { onRoleSelected(Role.TEACHER) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Препод",
                    color = if (selectedRole == Role.TEACHER) textColorSelected else textColorUnselected,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .fillMaxHeight()
                    .background(
                        if (selectedRole == Role.ADMIN) adminColor else Color.Transparent
                    )
                    .clickable { onRoleSelected(Role.ADMIN) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Админ",
                    color = if (selectedRole == Role.ADMIN) textColorSelected else textColorUnselected,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



