package com.example.springdddexample.application.service.task

import com.example.springdddexample.application.dto.task.CompleteTaskInput
import com.example.springdddexample.application.dto.task.CreateTaskInput
import com.example.springdddexample.application.dto.task.TaskOutput
import com.example.springdddexample.application.dto.task.UpdateTaskInput
import com.example.springdddexample.domain.model.task.Task
import com.example.springdddexample.domain.model.task.TaskId
import com.example.springdddexample.domain.model.task.TaskName
import com.example.springdddexample.domain.model.task.TaskRepository
import com.example.springdddexample.domain.model.task.TaskStatus
import com.example.springdddexample.domain.model.user.UserId
import com.example.springdddexample.domain.model.user.UserRepository
import com.example.springdddexample.domain.shared.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * タスクアプリケーションサービス
 * タスクに関するユースケースを統制する
 */
@Service
@Transactional
class TaskApplicationService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
) {
    /**
     * タスクを作成する
     */
    fun createTask(input: CreateTaskInput): TaskOutput {
        val userId = UserId(input.userId)

        // ユーザーの存在確認
        userRepository.findById(userId)
            ?: throw NotFoundException("ユーザーが見つかりません: ${input.userId}")

        val task =
            Task.create(
                userId = userId,
                name = TaskName(input.name),
                description = input.description,
            )

        val savedTask = taskRepository.save(task)
        return TaskOutput.from(savedTask)
    }

    /**
     * タスクを更新する
     */
    fun updateTask(input: UpdateTaskInput): TaskOutput {
        val taskId = TaskId(input.taskId)
        val existingTask =
            taskRepository.findById(taskId)
                ?: throw NotFoundException("タスクが見つかりません: ${input.taskId}")

        val updatedTask =
            existingTask.updateTask(
                newName = TaskName(input.name),
                newDescription = input.description,
            )

        val savedTask = taskRepository.save(updatedTask)
        return TaskOutput.from(savedTask)
    }

    /**
     * タスクを取得する
     */
    @Transactional(readOnly = true)
    fun getTask(id: String): TaskOutput {
        val taskId = TaskId(id)
        val task =
            taskRepository.findById(taskId)
                ?: throw NotFoundException("タスクが見つかりません: $id")

        return TaskOutput.from(task)
    }

    /**
     * すべてのタスクを取得する
     */
    @Transactional(readOnly = true)
    fun getAllTasks(): List<TaskOutput> =
        taskRepository
            .findAll()
            .map { TaskOutput.from(it) }

    /**
     * ユーザーのすべてのタスクを取得する
     */
    @Transactional(readOnly = true)
    fun getTasksByUserId(userId: String): List<TaskOutput> {
        val uid = UserId(userId)

        // ユーザーの存在確認
        userRepository.findById(uid)
            ?: throw NotFoundException("ユーザーが見つかりません: $userId")

        return taskRepository
            .findByUserId(uid)
            .map { TaskOutput.from(it) }
    }

    /**
     * ユーザーの指定された状態のタスクを取得する
     */
    @Transactional(readOnly = true)
    fun getTasksByUserIdAndStatus(
        userId: String,
        status: String,
    ): List<TaskOutput> {
        val uid = UserId(userId)

        // ユーザーの存在確認
        userRepository.findById(uid)
            ?: throw NotFoundException("ユーザーが見つかりません: $userId")

        val taskStatus = TaskStatus.valueOf(status.uppercase())

        return taskRepository
            .findByUserIdAndStatus(uid, taskStatus)
            .map { TaskOutput.from(it) }
    }

    /**
     * タスクを完了する
     */
    fun completeTask(input: CompleteTaskInput): TaskOutput {
        val taskId = TaskId(input.taskId)
        val existingTask =
            taskRepository.findById(taskId)
                ?: throw NotFoundException("タスクが見つかりません: ${input.taskId}")

        val completedTask = existingTask.complete()

        val savedTask = taskRepository.save(completedTask)
        return TaskOutput.from(savedTask)
    }

    /**
     * タスクを開始する
     */
    fun startTask(id: String): TaskOutput {
        val taskId = TaskId(id)
        val existingTask =
            taskRepository.findById(taskId)
                ?: throw NotFoundException("タスクが見つかりません: $id")

        val startedTask = existingTask.start()

        val savedTask = taskRepository.save(startedTask)
        return TaskOutput.from(savedTask)
    }

    /**
     * タスクをキャンセルする
     */
    fun cancelTask(id: String): TaskOutput {
        val taskId = TaskId(id)
        val existingTask =
            taskRepository.findById(taskId)
                ?: throw NotFoundException("タスクが見つかりません: $id")

        val cancelledTask = existingTask.cancel()

        val savedTask = taskRepository.save(cancelledTask)
        return TaskOutput.from(savedTask)
    }

    /**
     * タスクを削除する
     */
    fun deleteTask(id: String) {
        val taskId = TaskId(id)

        // タスクの存在確認
        taskRepository.findById(taskId)
            ?: throw NotFoundException("タスクが見つかりません: $id")

        taskRepository.delete(taskId)
    }

    /**
     * ユーザーのすべてのタスクを削除する（ユーザー削除時に使用）
     */
    fun deleteTasksByUserId(userId: String) {
        val uid = UserId(userId)
        taskRepository.deleteByUserId(uid)
    }
}
