package com.taskmanagerapp.taskmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.taskmanagerapp.taskmanager.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel = viewModel()) {
    val isDarkMode by settingsViewModel.darkModeFlow.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1976D2)),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { settingsViewModel.setDarkMode(it) }
                )
            }
        }
    }
}
