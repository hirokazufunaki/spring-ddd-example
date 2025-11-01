package com.example.springdddexample.domain.model.task

import com.example.springdddexample.domain.model.user.UserId
import com.example.springdddexample.domain.shared.BusinessRuleViolationException
import java.time.LocalDateTime

/**
 * Task集約ルート
 * タスクのライフサイクル全体を管理する
 */
data class Task(
    val id: TaskId,
    val userId: UserId,
    val name: TaskName,
    val description: String = "",
    val status: TaskStatus = TaskStatus.NOT_STARTED,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    /**
     * タスク情報を更新する
     * @param newName 新しいタスク名
     * @param newDescription 新しい説明
     * @return 更新されたタスク
     */
    fun updateTask(
        newName: TaskName,
        newDescription: String,
    ): Task {
        validateUpdate(newName, newDescription)

        return this.copy(
            name = newName,
            description = newDescription,
            updatedAt = LocalDateTime.now(),
        )
    }

    /**
     * タスク名のみ更新する
     */
    fun updateName(newName: TaskName): Task =
        this.copy(
            name = newName,
            updatedAt = LocalDateTime.now(),
        )

    /**
     * 説明のみ更新する
     */
    fun updateDescription(newDescription: String): Task =
        this.copy(
            description = newDescription,
            updatedAt = LocalDateTime.now(),
        )

    /**
     * タスクを完了状態に変更する
     * @return 更新されたタスク
     */
    fun complete(): Task {
        validateCanComplete()

        return this.copy(
            status = TaskStatus.COMPLETED,
            updatedAt = LocalDateTime.now(),
        )
    }

    /**
     * タスクを進行中状態に変更する
     * @return 更新されたタスク
     */
    fun start(): Task {
        validateCanStart()

        return this.copy(
            status = TaskStatus.IN_PROGRESS,
            updatedAt = LocalDateTime.now(),
        )
    }

    /**
     * タスクをキャンセルする
     * @return 更新されたタスク
     */
    fun cancel(): Task {
        validateCanCancel()

        return this.copy(
            status = TaskStatus.CANCELLED,
            updatedAt = LocalDateTime.now(),
        )
    }

    /**
     * タスク状態を変更する
     * @param newStatus 新しい状態
     * @return 更新されたタスク
     */
    fun changeStatus(newStatus: TaskStatus): Task {
        if (newStatus == TaskStatus.COMPLETED) {
            validateCanComplete()
        } else if (newStatus == TaskStatus.IN_PROGRESS) {
            validateCanStart()
        } else if (newStatus == TaskStatus.CANCELLED) {
            validateCanCancel()
        }

        return this.copy(
            status = newStatus,
            updatedAt = LocalDateTime.now(),
        )
    }

    private fun validateUpdate(
        newName: TaskName,
        newDescription: String,
    ) {
        // ビジネスルールのバリデーション
        if (newName.value == this.name.value && newDescription == this.description) {
            throw BusinessRuleViolationException("更新する内容がありません")
        }
    }

    private fun validateCanComplete() {
        if (this.status == TaskStatus.COMPLETED) {
            throw BusinessRuleViolationException("既に完了したタスクは完了状態に変更できません")
        }
        if (this.status == TaskStatus.CANCELLED) {
            throw BusinessRuleViolationException("キャンセルされたタスクは完了状態に変更できません")
        }
    }

    private fun validateCanStart() {
        if (this.status == TaskStatus.COMPLETED) {
            throw BusinessRuleViolationException("完了したタスクは進行中状態に変更できません")
        }
        if (this.status == TaskStatus.CANCELLED) {
            throw BusinessRuleViolationException("キャンセルされたタスクは進行中状態に変更できません")
        }
    }

    private fun validateCanCancel() {
        if (this.status == TaskStatus.COMPLETED) {
            throw BusinessRuleViolationException("完了したタスクはキャンセルできません")
        }
        if (this.status == TaskStatus.CANCELLED) {
            throw BusinessRuleViolationException("既にキャンセルされたタスクです")
        }
    }

    companion object {
        /**
         * 新しいタスクを作成する
         */
        fun create(
            userId: UserId,
            name: TaskName,
            description: String = "",
        ): Task =
            Task(
                id = TaskId.generate(),
                userId = userId,
                name = name,
                description = description,
            )
    }
}
