package com.taskmanagerapp.taskmanager.models

import androidx.room.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Int): Task?  // 🔹 Added this method

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    // ✅ Add this function to mark a task as complete
    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :taskId")
    suspend fun markTaskComplete(taskId: Int)

}