package com.taskmanagerapp.taskmanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.taskmanagerapp.taskmanager.models.Task

@Composable
fun TaskItem(
    task: Task,
    onComplete: () -> Unit,
    onDelete: () -> Unit
) {
    var isSwiped by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { isSwiped = true }
                )
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) Color(0xFFE0E0E0) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(if (isSwiped) Color.Gray.copy(alpha = 0.2f) else Color.Transparent),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                if (task.description?.isNotEmpty() == true) {
                    Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.alpha(0.7f)
                        )
                }
            }

            Row {
                IconButton(onClick = onComplete) {
                    Icon(Icons.Default.Check, contentDescription = "Mark as Complete", tint = Color.Green)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Task", tint = Color.Red)
                }
            }
        }
    }
}
