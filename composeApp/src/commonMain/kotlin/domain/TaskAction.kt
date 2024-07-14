package domain

/**
 * @author riezky maisyar
 */

sealed class TaskAction {
    data class Add(val task: TodoTask) : TaskAction()
    data class Update(val task: TodoTask) : TaskAction()
    data class Delete(val task: TodoTask) : TaskAction()
    data class SetComplete(val task: TodoTask, val taskCompleted: Boolean) : TaskAction()
    data class SetFavorite(val task: TodoTask, val isFavorite: Boolean) : TaskAction()
}