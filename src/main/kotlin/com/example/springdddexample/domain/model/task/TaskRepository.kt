package com.example.springdddexample.domain.model.task

import com.example.springdddexample.domain.model.user.UserId

/**
 * Taskリポジトリインターフェース（ポート）
 * ドメイン層でデータ永続化の契約を定義
 */
interface TaskRepository {
    /**
     * タスクを保存する
     * @param task 保存するタスク
     * @return 保存されたタスク
     */
    fun save(task: Task): Task

    /**
     * IDでタスクを検索する
     * @param id タスクID
     * @return 見つかったタスク、存在しない場合はnull
     */
    fun findById(id: TaskId): Task?

    /**
     * ユーザーのすべてのタスクを検索する
     * @param userId ユーザーID
     * @return タスクのリスト
     */
    fun findByUserId(userId: UserId): List<Task>

    /**
     * ユーザーの指定された状態のタスクを検索する
     * @param userId ユーザーID
     * @param status タスク状態
     * @return タスクのリスト
     */
    fun findByUserIdAndStatus(
        userId: UserId,
        status: TaskStatus,
    ): List<Task>

    /**
     * すべてのタスクを取得する
     * @return タスクのリスト
     */
    fun findAll(): List<Task>

    /**
     * タスクを削除する
     * @param id 削除するタスクのID
     */
    fun delete(id: TaskId)

    /**
     * ユーザーのタスクをすべて削除する（ユーザー削除時に使用）
     * @param userId ユーザーID
     */
    fun deleteByUserId(userId: UserId)

    /**
     * タスクが存在するかチェックする
     * @param id チェックするタスクID
     * @return 存在する場合true
     */
    fun existsById(id: TaskId): Boolean
}
