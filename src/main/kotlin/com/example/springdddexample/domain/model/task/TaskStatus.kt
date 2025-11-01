package com.example.springdddexample.domain.model.task

/**
 * タスク状態値オブジェクト（Enum）
 * タスクが取りうるすべての状態を定義する
 */
enum class TaskStatus(
    val displayName: String,
) {
    /**
     * 未着手
     */
    NOT_STARTED("未着手"),

    /**
     * 進行中
     */
    IN_PROGRESS("進行中"),

    /**
     * 完了
     */
    COMPLETED("完了"),

    /**
     * キャンセル
     */
    CANCELLED("キャンセル"),
}
