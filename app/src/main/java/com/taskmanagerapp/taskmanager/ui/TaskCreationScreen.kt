package com.taskmanagerapp.taskmanager.ui

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.taskmanagerapp.taskmanager.controller.TaskController
import com.taskmanagerapp.taskmanager.models.Task
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCreationScreen(navController: NavController, taskController: TaskController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Low") }
    var dueDate by remember { mutableStateOf("Select Due Date") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val calendar = Calendar.getInstance()

    // ✅ Working DatePickerDialog
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                dueDate = "$year-${month + 1}-$dayOfMonth"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    val priorities = listOf("Low", "Medium", "High")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Task", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // Title TextField
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description TextField
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Priority Dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = priority,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Priority") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    priorities.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                priority = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ Due Date TextField with Calendar Icon
            OutlinedTextField(
                value = dueDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Due Date") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date",
                            tint = Color(0xFF1976D2)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Task Button
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "Title is required!", Toast.LENGTH_SHORT).show()
                    } else {
                        coroutineScope.launch {
                            taskController.addTask(
                                Task(
                                    title = title,
                                    description = description,
                                    priority = priority,
                                    dueDate = dueDate
                                )
                            )
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Save Task", color = Color.White)
            }
        }
    }
}
