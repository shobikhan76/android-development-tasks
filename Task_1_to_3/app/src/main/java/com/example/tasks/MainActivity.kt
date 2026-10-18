package com.example.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.tasks.ui.theme.TasksTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TasksTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    StudentTasksApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun StudentTasksApp(modifier: Modifier = Modifier) {

    // Store all tasks
    val tasks = remember {
        mutableStateListOf(
            Task(
                id = 1,
                title = "Prepare Kotlin exercise",
                priority = Priority.HIGH
            ),
            Task(
                id = 2,
                title = "Read Android documentation",
                priority = Priority.MEDIUM
            ),
            Task(
                id = 3,
                title = "Run the Android app",
                priority = Priority.LOW,
                isCompleted = true
            )
        )
    }

    // Store text entered by the user
    var newTaskTitle by rememberSaveable {
        mutableStateOf("")
    }

    // Calculate completed tasks
    val completedCount = tasks.count { it.isCompleted }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // App title
        Text(
            text = "Student Tasks",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Course: Mobile Development",
            style = MaterialTheme.typography.bodyLarge
        )

        // Completed task count
        Text(
            text = "$completedCount of ${tasks.size} tasks completed",
            style = MaterialTheme.typography.bodyMedium
        )

        // Input and Add button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = {
                    Text("Enter task title")
                },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Button(
                onClick = {
                    val title = newTaskTitle.trim()

                    if (title.isNotEmpty()) {

                        // Generate a unique ID
                        val nextId =
                            (tasks.maxOfOrNull { it.id } ?: 0) + 1

                        // Add the new task
                        tasks.add(
                            Task(
                                id = nextId,
                                title = title,
                                priority = Priority.MEDIUM
                            )
                        )

                        // Clear the input
                        newTaskTitle = ""
                    }
                },
                enabled = newTaskTitle.isNotBlank()
            ) {
                Text("Add")
            }
        }

        Text(
            text = "My Student Tasks",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Display tasks
        if (tasks.isEmpty()) {

            Text("No tasks yet. Add your first task!")

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(
                    items = tasks,
                    key = { task -> task.id }
                ) { task ->

                    TaskRow(
                        task = task,

                        // Complete or uncomplete task
                        onCompletedChange = { checked ->

                            val index = tasks.indexOfFirst {
                                it.id == task.id
                            }

                            if (index != -1) {
                                tasks[index] = tasks[index].copy(
                                    isCompleted = checked
                                )
                            }
                        },

                        // Delete task
                        onDelete = {
                            tasks.remove(task)
                        }
                    )
                }
            }
        }
    }
}


// One row for each task
@Composable
fun TaskRow(
    task: Task,
    onCompletedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Checkbox
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCompletedChange
        )

        // Task title and priority
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {

            Text(
                text = task.title,
                textDecoration = if (task.isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )

            Text(
                text = "Priority: ${task.priority.name}",
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Delete button
        Button(
            onClick = onDelete
        ) {
            Text("Delete")
        }
    }
}