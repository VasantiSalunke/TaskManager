package com.taskmanagerapp.taskmanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.taskmanagerapp.taskmanager.controller.TaskController
import com.taskmanagerapp.taskmanager.models.Task
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.valentinilk.shimmer.shimmer

enum class SortOption { PRIORITY, DUE_DATE, ALPHABETICAL }
enum class FilterOption { ALL, COMPLETED, PENDING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, taskController: TaskController) {
    var tasks by remember { mutableStateOf<List<Task>?>(null) } // `null` means loading
    var selectedSort by remember { mutableStateOf(SortOption.DUE_DATE) }
    var selectedFilter by remember { mutableStateOf(FilterOption.ALL) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedSort, selectedFilter) {
        coroutineScope.launch {
            delay(2000) // Simulate network delay
            val allTasks = taskController.getAllTasks()

            val filteredTasks = when (selectedFilter) {
                FilterOption.ALL -> allTasks
                FilterOption.COMPLETED -> allTasks.filter { it.isCompleted }
                FilterOption.PENDING -> allTasks.filter { !it.isCompleted }
            }

            tasks = when (selectedSort) {
                SortOption.PRIORITY -> filteredTasks.sortedBy {
                    when (it.priority.lowercase()) {
                        "high" -> 1
                        "medium" -> 2
                        "low" -> 3
                        else -> 4
                    }
                }
                SortOption.DUE_DATE -> filteredTasks.sortedBy { it.dueDate }
                SortOption.ALPHABETICAL -> filteredTasks.sortedBy { it.title }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task List", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2)),
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("createTask") },
                containerColor = Color(0xFF1976D2)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            TaskFilterTabs(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it },
                selectedSort = selectedSort,
                onSortSelected = { selectedSort = it }
            )

            if (tasks == null) {
                ShimmerTaskList()
            } else if (tasks!!.isEmpty()) {
                Text(text = "No tasks available", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn {
                    items(tasks!!) { task -> TaskItem(task, navController) }
                }
            }
        }
    }
}

@Composable
fun ShimmerTaskList() {
    LazyColumn {
        items(5) { // Show 5 shimmer items
            ShimmerTaskItem()
        }
    }
}

@Composable
fun ShimmerTaskItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shimmer()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.LightGray)
                )
            }
        }
    }
}

@Composable
fun TaskFilterTabs(
    selectedFilter: FilterOption,
    onFilterSelected: (FilterOption) -> Unit,
    selectedSort: SortOption,
    onSortSelected: (SortOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterOption.values().forEach { filter ->
                val isSelected = filter == selectedFilter
                Button(
                    onClick = { onFilterSelected(filter) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFF1976D2) else Color.LightGray,
                        contentColor = if (isSelected) Color.White else Color.Black
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = filter.name,
                        style = TextStyle(
                            fontSize = getAdaptiveTextSize(filter.name) // Dynamic text size
                        )
                    )
                }
            }
        }

        SortMenu(selectedSort = selectedSort, onSortSelected = onSortSelected)
    }
}

fun getAdaptiveTextSize(text: String): TextUnit {
    return when {
        text.length > 10 -> 12.sp
        text.length > 6 -> 14.sp
        else -> 14.sp
    }
}

@Composable
fun SortMenu(selectedSort: SortOption, onSortSelected: (SortOption) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val iconColor = MaterialTheme.colorScheme.onSurface // Adapts to dark/light mode

    Box(
        modifier = Modifier.wrapContentSize()
    ) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(40.dp) // Ensures better visibility
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Sort Tasks",
                tint = iconColor, // Adapts dynamically
                modifier = Modifier.size(24.dp)
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            SortOption.values().forEach { sort ->
                DropdownMenuItem(
                    text = { Text("Sort by ${sort.name.replace("_", " ")}") },
                    onClick = {
                        onSortSelected(sort)
                        expanded = false
                    }
                )
            }
        }
    }
}



@Composable
fun TaskItem(task: Task, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("taskDetails/${task.id}") },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.title, style = MaterialTheme.typography.titleMedium)
            Text("Due Date: ${task.dueDate ?: "No Due Date"}", style = MaterialTheme.typography.bodySmall)
            Text("Priority: ${task.priority}", style = MaterialTheme.typography.bodySmall)
            Text("Status: ${if (task.isCompleted) "Completed" else "Pending"}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
