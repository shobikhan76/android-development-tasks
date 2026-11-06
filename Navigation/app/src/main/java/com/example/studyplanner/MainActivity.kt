
package com.example.studyplanner

import android.net.Uri
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.studyplanner.ui.theme.NavigationTheme


// ========================================
// 1. MAIN ACTIVITY
// ========================================

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                StudyPlanner()
            }
        }
    }
}


// ========================================
// 2. HOME SCREEN
// ========================================

@Composable
fun HomeScreen(
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Study Planner",
            style = MaterialTheme.typography.headlineMedium
        )

        if (tasks.isEmpty()) {

            Text("No tasks yet")

        } else {

            tasks.forEach { task ->

                TaskRow(
                    task = task,
                    onClick = {
                        onTaskClick(task)
                    }
                )
            }
        }
    }
}


// ========================================
// 3. TASK ROW COMPONENT
// ========================================

@Composable
fun TaskRow(
    task: Task,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(task.title)

                Text(task.priority.name)
            }

            if (task.isCompleted) Text("Done") else {

                Text("Pending")
            }
        }
    }
}


// ========================================
// 4. TASK DETAIL SCREEN
// ========================================

@Composable
fun TaskDetailScreen(
    taskId: Int,
    title: String,
    isCompleted: Boolean,
    priority: String,
    onBack: () -> Unit,
    onToggle: (Boolean) -> Unit
) {

    var completed by remember {
        mutableStateOf(isCompleted)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Task details",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("ID: $taskId")

        Text("Title: $title")

        Text(
            "Completed: ${if (completed) "Yes" else "No"}"
        )

        Text("Priority: $priority")


        // Toggle completion status

        Button(
            onClick = {

                completed = !completed

                onToggle(completed)
            }
        ) {

            Text(
                if (completed)
                    "Mark as pending"
                else
                    "Mark as completed"
            )
        }


        // Back button

        OutlinedButton(
            onClick = onBack
        ) {

            Text("Back")
        }
    }
}


// ========================================
// 5. MAIN APPLICATION
// ========================================

@Composable
fun StudyPlanner() {

    // Navigation controller

    val navController = rememberNavController()


    // Shared task state

    var tasks by remember {

        mutableStateOf(

            listOf(

                Task(
                    1,
                    "Study for my exam",
                    false,
                    Priority.HIGH
                ),

                Task(
                    2,
                    "Buy groceries",
                    true,
                    Priority.MEDIUM
                ),

                Task(
                    3,
                    "Read a book",
                    false,
                    Priority.LOW
                )
            )
        )
    }


    // Navigation host

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {


        // HOME DESTINATION

        composable("home") {

            HomeScreen(

                tasks = tasks,

                onTaskClick = { task ->

                    navController.navigate(

                        "taskDetail/${task.id}/${Uri.encode(task.title)}/${task.isCompleted}/${task.priority.name}"

                    )
                }
            )
        }


        // DETAIL DESTINATION

        composable(

            route = "taskDetail/{taskId}/{title}/{isCompleted}/{priority}",

            arguments = listOf(

                navArgument("taskId") {
                    type = NavType.IntType
                },

                navArgument("title") {
                    type = NavType.StringType
                },

                navArgument("isCompleted") {
                    type = NavType.BoolType
                },

                navArgument("priority") {
                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->


            // Retrieve navigation arguments

            val taskId =
                backStackEntry.arguments?.getInt("taskId") ?: 0

            val title =
                backStackEntry.arguments?.getString("title") ?: ""

            val isCompleted =
                backStackEntry.arguments?.getBoolean("isCompleted") ?: false

            val priority =
                backStackEntry.arguments?.getString("priority") ?: "MEDIUM"


            // Display detail screen

            TaskDetailScreen(

                taskId = taskId,

                title = title,

                isCompleted = isCompleted,

                priority = priority,


                // Navigate back

                onBack = {
                    navController.popBackStack()
                },


                // Update selected task

                onToggle = { newValue ->

                    tasks = tasks.map { task ->

                        if (task.id == taskId) {

                            task.copy(
                                isCompleted = newValue
                            )

                        } else {

                            task
                        }
                    }
                }
            )
        }
    }
}
