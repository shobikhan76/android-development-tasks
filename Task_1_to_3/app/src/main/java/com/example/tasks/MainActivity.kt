
package com.example.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
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

        val courseName = "Mobile Development"
        val completedTasks = 0
        val maxTasks = 10

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

    var newTaskTitle by rememberSaveable {
        mutableStateOf("")
    }

    val completedCount = tasks.count { it.isCompleted }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AppTitle()

        Text(
            text = "Course: Mobile Development",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "$completedCount of ${tasks.size} tasks completed",
            style = MaterialTheme.typography.bodyMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = newTaskTitle,
                onValueChange = { newTaskTitle = it },
                label = { Text("Enter task title") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            Button(
                onClick = {
                    val title = newTaskTitle.trim()

                    if (title.isNotEmpty()) {
                        val nextId =
                            (tasks.maxOfOrNull { it.id } ?: 0) + 1

                        tasks.add(
                            Task(
                                id = nextId,
                                title = title,
                                priority = Priority.MEDIUM
                            )
                        )

                        newTaskTitle = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Text(
            text = "My Student Tasks",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        if (tasks.isEmpty()) {
            Text("No tasks yet. Add your first task!")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = tasks,
                    key = { task -> task.id }
                ) { task ->
                    TaskRow(
                        title = task.title,
                        isCompleted = task.isCompleted,
                        priority = task.priority,
                        onCompletedChange = { checked ->
                            val index = tasks.indexOfFirst {
                                it.id == task.id
                            }

                            if (index != -1) {
                                tasks[index] =
                                    tasks[index].copy(
                                        isCompleted = checked
                                    )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppTitle(modifier: Modifier = Modifier) {
    Text(
        text = "Student Tasks",
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun TaskRow(
    title: String,
    isCompleted: Boolean,
    priority: Priority,
    onCompletedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isCompleted,
            onCheckedChange = onCompletedChange
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(
                text = title,
                textDecoration = if (isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                }
            )

            Text(
                text = "Priority: ${priority.name}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
