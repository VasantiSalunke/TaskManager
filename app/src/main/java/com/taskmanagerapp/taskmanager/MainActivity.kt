package com.taskmanagerapp.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taskmanagerapp.taskmanager.controller.TaskController
import com.taskmanagerapp.taskmanager.data.DataStoreManager
import com.taskmanagerapp.taskmanager.ui.SettingsScreen
import com.taskmanagerapp.taskmanager.ui.TaskCreationScreen
import com.taskmanagerapp.taskmanager.ui.TaskDetailsScreen
import com.taskmanagerapp.taskmanager.ui.TaskListScreen
import com.taskmanagerapp.taskmanager.ui.theme.TaskManagerTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskController = TaskController(this)
        val dataStoreManager = DataStoreManager(this)


        lifecycleScope.launch {
            val isDarkMode = dataStoreManager.darkModeFlow.first() // Get saved theme
            setContent {
                val darkTheme by dataStoreManager.darkModeFlow.collectAsState(initial = isDarkMode)
                val navController: NavHostController = rememberNavController()

                SideEffect {
                    window?.statusBarColor = Color(0xFF1976D2).toArgb() // Set to match TopAppBar
                    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
                }


                TaskManagerTheme(darkTheme) {
                    NavHost(navController, startDestination = "taskList") {
                        composable("taskList") { TaskListScreen(navController, taskController) }
                        composable("createTask") { TaskCreationScreen(navController, taskController) }
                        composable("settings") { SettingsScreen(navController) }
                        composable("taskDetails/{taskId}") { backStackEntry ->
                            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()

                            if (taskId != null) {
                                TaskDetailsScreen(
                                    taskId = taskId,
                                    taskController = taskController,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
