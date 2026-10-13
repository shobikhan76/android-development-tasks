
package com.example.tasks

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}

data class Task(
    val id: Int,
    val title: String,
    val priority: Priority = Priority.MEDIUM,
    val isCompleted: Boolean = false
)

fun describeTask(task: Task): String {
    return "${task.title} - ${task.priority}"
}
