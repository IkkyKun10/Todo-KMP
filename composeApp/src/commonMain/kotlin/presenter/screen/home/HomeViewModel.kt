package presenter.screen.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.RequestState
import domain.TaskAction
import domain.TodoTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

/**
 * @author riezky maisyar
 */

typealias MutableTasks = MutableState<RequestState<List<TodoTask>>>
typealias Tasks = MutableState<RequestState<List<TodoTask>>>

class HomeViewModel(private val mongoDB: MongoDB) : ScreenModel {
    private var _activeTask: MutableTasks = mutableStateOf(RequestState.Idle)
    val activeTask: Tasks = _activeTask

    private var _completedTask: MutableTasks = mutableStateOf(RequestState.Idle)
    val completedTask: Tasks = _completedTask

    init {
        _activeTask.value = RequestState.Loading
        _completedTask.value = RequestState.Loading
        screenModelScope.launch(Dispatchers.Main) {
            delay(500)
            mongoDB.readActiveTasks().collectLatest {
                _activeTask.value = it
            }
        }
        screenModelScope.launch(Dispatchers.Main) {
            delay(500)
            mongoDB.readCompletedTasks().collectLatest {
                _completedTask.value = it
            }
        }
    }

    fun setAction(taskAction: TaskAction) {
        when (taskAction) {
            is TaskAction.Add -> {

            }
            is TaskAction.Update -> {

            }
            is TaskAction.Delete -> {
                deleteTask(taskAction.task)
            }
            is TaskAction.SetComplete -> {
                setCompleted(taskAction.task, taskAction.taskCompleted)
            }
            is TaskAction.SetFavorite -> {
                setFavorite(taskAction.task, taskAction.isFavorite)
            }
        }
    }

    private fun setFavorite(task: TodoTask, isFavorite: Boolean) {
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.setFavorite(task, isFavorite)
        }
    }

    private fun setCompleted(task: TodoTask, taskCompleted: Boolean) {
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.setCompleted(task, taskCompleted)
        }
    }

    private fun deleteTask(task: TodoTask) {
        screenModelScope.launch(Dispatchers.Main) {
            mongoDB.deleteTask(task)
        }
    }
}