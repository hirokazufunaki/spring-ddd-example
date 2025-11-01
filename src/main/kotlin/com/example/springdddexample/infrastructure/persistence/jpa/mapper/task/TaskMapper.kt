package com.example.springdddexample.infrastructure.persistence.jpa.mapper.task

import com.example.springdddexample.domain.model.task.Task
import com.example.springdddexample.domain.model.task.TaskId
import com.example.springdddexample.domain.model.task.TaskName
import com.example.springdddexample.domain.model.task.TaskStatus
import com.example.springdddexample.domain.model.user.UserId
import com.example.springdddexample.infrastructure.persistence.jpa.entity.task.TaskJpaEntity
import com.example.springdddexample.infrastructure.persistence.jpa.entity.task.TaskStatusEntity
import org.springframework.stereotype.Component

/**
 * ドメインオブジェクトとJPAエンティティ間の変換を行うマッパー
 */
@Component
class TaskMapper {
    /**
     * ドメインオブジェクトからJPAエンティティに変換
     */
    fun toJpaEntity(task: Task): TaskJpaEntity =
        TaskJpaEntity(
            id = task.id.value,
            userId = task.userId.value,
            name = task.name.value,
            description = task.description,
            status = taskStatusToEntity(task.status),
            createdAt = task.createdAt,
            updatedAt = task.updatedAt,
        )

    /**
     * JPAエンティティからドメインオブジェクトに変換
     */
    fun toDomainObject(entity: TaskJpaEntity): Task =
        Task(
            id = TaskId(entity.id),
            userId = UserId(entity.userId),
            name = TaskName(entity.name),
            description = entity.description,
            status = taskStatusToDomain(entity.status),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    /**
     * JPAエンティティのリストからドメインオブジェクトのリストに変換
     */
    fun toDomainObjects(entities: List<TaskJpaEntity>): List<Task> = entities.map { toDomainObject(it) }

    /**
     * ドメインのTaskStatusをJPAのTaskStatusEntityに変換
     */
    private fun taskStatusToEntity(status: TaskStatus): TaskStatusEntity =
        when (status) {
            TaskStatus.NOT_STARTED -> TaskStatusEntity.NOT_STARTED
            TaskStatus.IN_PROGRESS -> TaskStatusEntity.IN_PROGRESS
            TaskStatus.COMPLETED -> TaskStatusEntity.COMPLETED
            TaskStatus.CANCELLED -> TaskStatusEntity.CANCELLED
        }

    /**
     * JPAのTaskStatusEntityをドメインのTaskStatusに変換
     */
    private fun taskStatusToDomain(status: TaskStatusEntity): TaskStatus =
        when (status) {
            TaskStatusEntity.NOT_STARTED -> TaskStatus.NOT_STARTED
            TaskStatusEntity.IN_PROGRESS -> TaskStatus.IN_PROGRESS
            TaskStatusEntity.COMPLETED -> TaskStatus.COMPLETED
            TaskStatusEntity.CANCELLED -> TaskStatus.CANCELLED
        }
}
