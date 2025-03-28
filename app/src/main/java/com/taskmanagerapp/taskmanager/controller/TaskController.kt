package com.taskmanagerapp.taskmanager.controller

import android.content.Context
import com.taskmanagerapp.taskmanager.models.Task
import com.taskmanagerapp.taskmanager.models.TaskDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskController(context: Context) {
    private val taskDao = TaskDatabase.getDatabase(context).taskDao()

    suspend fun addTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }

    suspend fun getAllTasks(): List<Task> {
        return withContext(Dispatchers.IO) {
            taskDao.getAllTasks()
        }
    }

    suspend fun updateTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.updateTask(task)
        }
    }

    suspend fun deleteTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(task)
        }
    }

    suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId) // Assuming you have a Room DAO
    }

    suspend fun markTaskComplete(taskId: Int) = taskDao.markTaskComplete(taskId)

}
