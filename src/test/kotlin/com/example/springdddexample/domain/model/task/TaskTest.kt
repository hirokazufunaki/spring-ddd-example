package com.example.springdddexample.domain.model.task

import com.example.springdddexample.domain.model.user.UserId
import com.example.springdddexample.domain.shared.BusinessRuleViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Duration

/**
 * Task集約ルートの単体テスト
 */
class TaskTest {
    @Test
    fun `正常な値でTaskが作成できること`() {
        // Arrange
        val userId = UserId.generate()
        val name = TaskName("買い物")
        val description = "牛乳とパンを買う"

        // Act
        val task = Task.create(userId, name, description)

        // Assert
        assertNotNull(task.id)
        assertEquals(userId, task.userId)
        assertEquals(name, task.name)
        assertEquals(description, task.description)
        assertEquals(TaskStatus.NOT_STARTED, task.status)
        assertNotNull(task.createdAt)
        assertNotNull(task.updatedAt)
        assertTrue(task.createdAt.isBefore(task.updatedAt.plus(Duration.ofMillis(1))))
    }

    @Test
    fun `タスク情報が更新できること`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val newName = TaskName("買い物（変更）")
        val newDescription = "牛乳とパンとバターを買う"
        val originalUpdatedAt = task.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val updatedTask = task.updateTask(newName, newDescription)

        // Assert
        assertEquals(task.id, updatedTask.id)
        assertEquals(task.userId, updatedTask.userId)
        assertEquals(newName, updatedTask.name)
        assertEquals(newDescription, updatedTask.description)
        assertEquals(task.createdAt, updatedTask.createdAt)
        assertTrue(updatedTask.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `タスク名のみ更新できること`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val newName = TaskName("買い物（変更）")
        val originalUpdatedAt = task.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val updatedTask = task.updateName(newName)

        // Assert
        assertEquals(task.id, updatedTask.id)
        assertEquals(newName, updatedTask.name)
        assertEquals(task.description, updatedTask.description)
        assertEquals(task.createdAt, updatedTask.createdAt)
        assertTrue(updatedTask.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `説明のみ更新できること`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val newDescription = "牛乳とパンとバターを買う"
        val originalUpdatedAt = task.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val updatedTask = task.updateDescription(newDescription)

        // Assert
        assertEquals(task.id, updatedTask.id)
        assertEquals(task.name, updatedTask.name)
        assertEquals(newDescription, updatedTask.description)
        assertEquals(task.createdAt, updatedTask.createdAt)
        assertTrue(updatedTask.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `タスクを完了できること`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val originalUpdatedAt = task.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val completedTask = task.complete()

        // Assert
        assertEquals(task.id, completedTask.id)
        assertEquals(TaskStatus.COMPLETED, completedTask.status)
        assertTrue(completedTask.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `タスクを開始できること`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val originalUpdatedAt = task.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val startedTask = task.start()

        // Assert
        assertEquals(task.id, startedTask.id)
        assertEquals(TaskStatus.IN_PROGRESS, startedTask.status)
        assertTrue(startedTask.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `タスクをキャンセルできること`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val originalUpdatedAt = task.updatedAt

        // 時間の差を確実にするため少し待機
        Thread.sleep(1)

        // Act
        val cancelledTask = task.cancel()

        // Assert
        assertEquals(task.id, cancelledTask.id)
        assertEquals(TaskStatus.CANCELLED, cancelledTask.status)
        assertTrue(cancelledTask.updatedAt.isAfter(originalUpdatedAt))
    }

    @Test
    fun `同じ値でタスク更新するとエラーになること`() {
        // Arrange
        val name = TaskName("買い物")
        val description = "牛乳とパンを買う"
        val task = Task.create(UserId.generate(), name, description)

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                task.updateTask(name, description)
            }
        assertEquals("更新する内容がありません", exception.message)
    }

    @Test
    fun `完了済みのタスクは完了状態に変更できないこと`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val completedTask = task.complete()

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                completedTask.complete()
            }
        assertEquals("既に完了したタスクは完了状態に変更できません", exception.message)
    }

    @Test
    fun `完了済みのタスクは進行中状態に変更できないこと`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val completedTask = task.complete()

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                completedTask.start()
            }
        assertEquals("完了したタスクは進行中状態に変更できません", exception.message)
    }

    @Test
    fun `完了済みのタスクはキャンセルできないこと`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val completedTask = task.complete()

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                completedTask.cancel()
            }
        assertEquals("完了したタスクはキャンセルできません", exception.message)
    }

    @Test
    fun `キャンセル済みのタスクは完了状態に変更できないこと`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val cancelledTask = task.cancel()

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                cancelledTask.complete()
            }
        assertEquals("キャンセルされたタスクは完了状態に変更できません", exception.message)
    }

    @Test
    fun `キャンセル済みのタスクは進行中状態に変更できないこと`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val cancelledTask = task.cancel()

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                cancelledTask.start()
            }
        assertEquals("キャンセルされたタスクは進行中状態に変更できません", exception.message)
    }

    @Test
    fun `キャンセル済みのタスクは再度キャンセルできないこと`() {
        // Arrange
        val task = Task.create(UserId.generate(), TaskName("買い物"), "牛乳とパンを買う")
        val cancelledTask = task.cancel()

        // Act & Assert
        val exception =
            assertThrows<BusinessRuleViolationException> {
                cancelledTask.cancel()
            }
        assertEquals("既にキャンセルされたタスクです", exception.message)
    }
}
