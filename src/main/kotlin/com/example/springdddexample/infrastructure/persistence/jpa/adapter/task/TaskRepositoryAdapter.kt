package com.example.springdddexample.infrastructure.persistence.jpa.adapter.task

import com.example.springdddexample.domain.model.task.Task
import com.example.springdddexample.domain.model.task.TaskId
import com.example.springdddexample.domain.model.task.TaskRepository
import com.example.springdddexample.domain.model.task.TaskStatus
import com.example.springdddexample.domain.model.user.UserId
import com.example.springdddexample.infrastructure.persistence.jpa.entity.task.TaskStatusEntity
import com.example.springdddexample.infrastructure.persistence.jpa.mapper.task.TaskMapper
import com.example.springdddexample.infrastructure.persistence.jpa.repository.task.TaskJpaRepository
import org.springframework.stereotype.Repository

/**
 * タスクリポジトリアダプター
 * ドメインのTaskRepositoryインターフェースの実装
 */
@Repository
class TaskRepositoryAdapter(
    private val taskJpaRepository: TaskJpaRepository,
    private val taskMapper: TaskMapper,
) : TaskRepository {
    override fun save(task: Task): Task {
        val entity = taskMapper.toJpaEntity(task)
        val savedEntity = taskJpaRepository.save(entity)
        return taskMapper.toDomainObject(savedEntity)
    }

    override fun findById(id: TaskId): Task? =
        taskJpaRepository
            .findById(id.value)
            .map { taskMapper.toDomainObject(it) }
            .orElse(null)

    override fun findByUserId(userId: UserId): List<Task> {
        val entities = taskJpaRepository.findByUserId(userId.value)
        return taskMapper.toDomainObjects(entities)
    }

    override fun findByUserIdAndStatus(
        userId: UserId,
        status: TaskStatus,
    ): List<Task> {
        val statusEntity = taskStatusToEntity(status)
        val entities = taskJpaRepository.findByUserIdAndStatus(userId.value, statusEntity)
        return taskMapper.toDomainObjects(entities)
    }

    override fun findAll(): List<Task> {
        val entities = taskJpaRepository.findAll()
        return taskMapper.toDomainObjects(entities)
    }

    override fun delete(id: TaskId) {
        taskJpaRepository.deleteById(id.value)
    }

    override fun deleteByUserId(userId: UserId) {
        taskJpaRepository.deleteByUserId(userId.value)
    }

    override fun existsById(id: TaskId): Boolean = taskJpaRepository.existsById(id.value)

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
}
