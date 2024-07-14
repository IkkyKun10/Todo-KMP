package presenter.screen.task

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import domain.TaskAction
import domain.TodoTask

/**
 * @author riezky maisyar
 */

const val DEFAULT_TITLE = "Enter the Title"
const val DEFAULT_DESCRIPTION = "Add some description"

data class TaskScreen(val todoTask: TodoTask? = null) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<TaskViewModel>()
        var currentTitle by remember {
            mutableStateOf(todoTask?.title ?: DEFAULT_TITLE)
        }
        var currentDescription by remember {
            mutableStateOf(todoTask?.description ?: DEFAULT_DESCRIPTION)
        }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        BasicTextField(
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize
                            ),
                            singleLine = true,
                            value = currentTitle,
                            onValueChange = {
                                currentTitle = it
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back Icon"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                if (currentTitle.isNotEmpty() && currentDescription.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = {
                            if (todoTask != null) {
                                viewModel.setAction(
                                    TaskAction.Update(
                                        TodoTask().apply {
                                            _id = todoTask._id
                                            title = currentTitle
                                            description = currentDescription
                                        }
                                    )
                                )
                            } else {
                                viewModel.setAction(
                                    TaskAction.Add(
                                        TodoTask().apply {
                                            title = currentTitle
                                            description = currentDescription
                                        }
                                    )
                                )
                            }
                            navigator.pop()
                        },
                        shape = RoundedCornerShape(size = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checkmark Icon"
                        )
                    }
                }
            }
        ) { paddingValues ->
            BasicTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 24.dp)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                value = currentDescription,
                onValueChange = {
                    currentDescription = it
                }
            )
        }
    }
}