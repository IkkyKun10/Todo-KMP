@file:OptIn(ExperimentalMaterial3Api::class)

package presenter.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.RequestState
import domain.TaskAction
import domain.TodoTask
import presenter.component.ErrorScreen
import presenter.component.LoadingScreen
import presenter.component.TaskView
import presenter.screen.task.TaskScreen

/**
 * @author riezky maisyar
 */

class HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<HomeViewModel>()
        val activeTask by viewModel.activeTask
        val completedTask by viewModel.completedTask

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = {
                    Text(text = "Home")
                })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigator.push(TaskScreen())},
                    shape = RoundedCornerShape(size = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Icon"
                    )
                }
            }
        ) { padding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
            ) {
                DisplayTask(
                    modifier = Modifier.weight(1f),
                    task = activeTask,
                    onSelect = { selectedTask ->
                        navigator.push(TaskScreen(todoTask = selectedTask))
                    },
                    onFavorite = { task, isFavorite ->
                        viewModel.setAction(
                            taskAction = TaskAction.SetFavorite(task, isFavorite)
                        )
                    },
                    onCompleted = { task, completed ->
                        viewModel.setAction(
                            taskAction = TaskAction.SetComplete(task, completed)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                DisplayTask(
                    modifier = Modifier.weight(1f),
                    task = completedTask,
                    showActive = false,
                    onCompleted = { task, completed ->
                        viewModel.setAction(
                            taskAction = TaskAction.SetComplete(task, completed)
                        )
                    },
                    onDeleted = { task ->
                        viewModel.setAction(
                            taskAction = TaskAction.Delete(task)
                        )
                    }
                )
            }
        }
    }


    @Composable
    fun DisplayTask(
        modifier: Modifier = Modifier,
        task: RequestState<List<TodoTask>>,
        showActive: Boolean = true,
        onSelect: ((TodoTask) -> Unit)? = null,
        onFavorite: ((TodoTask, Boolean) -> Unit)? = null,
        onCompleted: (TodoTask, Boolean) -> Unit,
        onDeleted: ((TodoTask) -> Unit)? = null
    ) {
        var showDialog by remember { mutableStateOf(false) }
        var taskToDelete: TodoTask? by remember { mutableStateOf(null) }

        if (showDialog) {
            AlertDialog(
                title = {
                    Text(text = "Delete", fontSize = MaterialTheme.typography.titleLarge.fontSize)
                },
                text = {
                    Text(
                        text = "Are you sure you want to remove '${taskToDelete!!.title}' task?",
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            taskToDelete?.let { onDeleted?.invoke(it) }
                            showDialog = false
                            taskToDelete = null
                        }
                    ) {
                        Text(text = "Yes")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            taskToDelete = null
                        }
                    ) {
                        Text(text = "Cancel")
                    }
                },
                onDismissRequest = {
                    taskToDelete = null
                    showDialog = false
                }
            )
        }

        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp),
                text = if (showActive) "Active Tasks" else "Completed Tasks",
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(12.dp))
            task.DisplayResult(
                onLoading = { LoadingScreen() },
                onError = { ErrorScreen(message = it) },
                onSuccess = {
                    if (it.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
                            items(
                                items = it,
                                key = { task ->
                                    task._id.toHexString()
                                }
                            ) { task ->
                                TaskView(
                                    showActive = showActive,
                                    task = task,
                                    onSelect = { onSelect?.invoke(task) },
                                    onComplete = { selectedTask, completed ->
                                        onCompleted(selectedTask, completed)
                                    },
                                    onFavorite = { selectedTask, favorite ->
                                        onFavorite?.invoke(selectedTask, favorite)
                                    },
                                    onDelete = { selectedTask ->
                                        taskToDelete = selectedTask
                                        showDialog = true
                                    }
                                )
                            }
                        }
                    } else {
                        ErrorScreen(null)
                    }
                }
            )
        }
    }
}