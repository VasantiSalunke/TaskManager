package com.taskmanagerapp.taskmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.taskmanagerapp.taskmanager.controller.TaskController
import com.taskmanagerapp.taskmanager.models.Task
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(taskId: Int, taskController: TaskController, navController: NavController) {
    var task by remember { mutableStateOf<Task?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(taskId) {
        task = taskController.getTaskById(taskId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2))
            )
        }
    ) { paddingValues ->
        task?.let { currentTask ->
            var isCompleted by remember { mutableStateOf(currentTask.isCompleted) }

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = currentTask.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Description:", fontWeight = FontWeight.Bold)
                Text(text = currentTask.description ?: "No Description")
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Priority:", fontWeight = FontWeight.Bold)
                Text(text = currentTask.priority)
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Due Date:", fontWeight = FontWeight.Bold)
                Text(text = currentTask.dueDate)
                Spacer(modifier = Modifier.height(16.dp))

                // Row for Complete & Delete Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                taskController.markTaskComplete(currentTask.id)
                                isCompleted = true
                            }
                        },
                        enabled = !isCompleted, // Disable if already completed
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCompleted) Color.Gray else Color(0xFF4CAF50) // Green when active
                        )
                    ) {
                        Text(if (isCompleted) "Completed" else "Mark as Complete")
                    }

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                taskController.deleteTask(currentTask)
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete Task", color = Color.White)
                    }
                }
            }
        } ?: Text(
            text = "Loading task details...",
            modifier = Modifier.padding(16.dp)
        )
    }
}
