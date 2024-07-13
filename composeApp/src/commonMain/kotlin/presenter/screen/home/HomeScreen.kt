@file:OptIn(ExperimentalMaterial3Api::class)

package presenter.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.RequestState
import domain.TodoTask
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
    }
}