package presenter.screen.task

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import data.MongoDB
import domain.TaskAction
import domain.TodoTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

/**
 * @author riezky maisyar
 */

class TaskViewModel(private val mongoDB: MongoDB) : ScreenModel {

    fun setAction(taskAction: TaskAction) {
        when (taskAction) {
            is TaskAction.Add -> {
                addTask(taskAction.task)
            }
            is TaskAction.Update -> {
                updateTask(taskAction.task)
            }
            else -> {}
        }
    }

    private fun addTask(todoTask: TodoTask) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.addTask(todoTask)
        }
    }

    private fun updateTask(todoTask: TodoTask) {
        screenModelScope.launch(Dispatchers.IO) {
            mongoDB.updateTask(todoTask)
        }
    }
}