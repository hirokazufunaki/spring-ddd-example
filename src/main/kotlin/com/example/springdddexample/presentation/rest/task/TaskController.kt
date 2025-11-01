package com.example.springdddexample.presentation.rest.task

import com.example.springdddexample.application.dto.task.CompleteTaskInput
import com.example.springdddexample.application.service.task.TaskApplicationService
import com.example.springdddexample.presentation.dto.task.CreateTaskRequest
import com.example.springdddexample.presentation.dto.task.TaskListResponse
import com.example.springdddexample.presentation.dto.task.TaskResponse
import com.example.springdddexample.presentation.dto.task.UpdateTaskRequest
import com.example.springdddexample.presentation.exception.ErrorResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * タスクREST API コントローラー
 * タスクの登録、参照、更新、削除のHTTPエンドポイントを提供
 */
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "タスク管理", description = "タスクの登録、参照、更新、削除を行うAPI")
class TaskController(
    private val taskApplicationService: TaskApplicationService,
) {
    /**
     * タスクを作成する
     * POST /api/tasks
     */
    @PostMapping
    @Operation(
        summary = "タスク作成",
        description = "新しいタスクを作成します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "タスクが正常に作成されました",
                content = [Content(schema = Schema(implementation = TaskResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "バリデーションエラーまたはビジネスルール違反",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたユーザーが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun createTask(
        @Valid @RequestBody request: CreateTaskRequest,
    ): ResponseEntity<TaskResponse> {
        val taskResult = taskApplicationService.createTask(request.toInput())
        val response = TaskResponse.from(taskResult)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * タスクを取得する
     * GET /api/tasks/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "タスク取得",
        description = "指定されたIDのタスクを取得します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "タスクが正常に取得されました",
                content = [Content(schema = Schema(implementation = TaskResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたタスクが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun getTask(
        @Parameter(description = "タスクID", required = true)
        @PathVariable id: String,
    ): ResponseEntity<TaskResponse> {
        val taskResult = taskApplicationService.getTask(id)
        val response = TaskResponse.from(taskResult)
        return ResponseEntity.ok(response)
    }

    /**
     * すべてのタスクを取得する
     * GET /api/tasks
     */
    @GetMapping
    @Operation(
        summary = "全タスク取得",
        description = "登録されているすべてのタスクを取得します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "タスクリストが正常に取得されました",
                content = [Content(schema = Schema(implementation = TaskListResponse::class))],
            ),
        ],
    )
    fun getAllTasks(): ResponseEntity<TaskListResponse> {
        val taskResults = taskApplicationService.getAllTasks()
        val responses = taskResults.map { TaskResponse.from(it) }
        val response = TaskListResponse.from(responses)
        return ResponseEntity.ok(response)
    }

    /**
     * ユーザーのタスクを取得する
     * GET /api/tasks/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @Operation(
        summary = "ユーザーのタスク取得",
        description = "指定されたユーザーのすべてのタスクを取得します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "タスクリストが正常に取得されました",
                content = [Content(schema = Schema(implementation = TaskListResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたユーザーが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun getTasksByUserId(
        @Parameter(description = "ユーザーID", required = true)
        @PathVariable userId: String,
        @Parameter(description = "タスク状態（オプション）", required = false)
        @RequestParam(required = false) status: String?,
    ): ResponseEntity<TaskListResponse> {
        val taskResults =
            if (status != null) {
                taskApplicationService.getTasksByUserIdAndStatus(userId, status)
            } else {
                taskApplicationService.getTasksByUserId(userId)
            }
        val responses = taskResults.map { TaskResponse.from(it) }
        val response = TaskListResponse.from(responses)
        return ResponseEntity.ok(response)
    }

    /**
     * タスクを更新する
     * PUT /api/tasks/{id}
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "タスク更新",
        description = "指定されたIDのタスク情報を更新します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "タスクが正常に更新されました",
                content = [Content(schema = Schema(implementation = TaskResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "バリデーションエラーまたはビジネスルール違反",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたタスクが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun updateTask(
        @Parameter(description = "タスクID", required = true)
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateTaskRequest,
    ): ResponseEntity<TaskResponse> {
        val taskResult = taskApplicationService.updateTask(request.toInput(id))
        val response = TaskResponse.from(taskResult)
        return ResponseEntity.ok(response)
    }

    /**
     * タスクを完了する
     * POST /api/tasks/{id}/complete
     */
    @PostMapping("/{id}/complete")
    @Operation(
        summary = "タスク完了",
        description = "指定されたIDのタスクを完了状態に変更します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "タスクが正常に完了しました",
                content = [Content(schema = Schema(implementation = TaskResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "ビジネスルール違反（既に完了済みなど）",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたタスクが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun completeTask(
        @Parameter(description = "タスクID", required = true)
        @PathVariable id: String,
    ): ResponseEntity<TaskResponse> {
        val taskResult = taskApplicationService.completeTask(CompleteTaskInput(id))
        val response = TaskResponse.from(taskResult)
        return ResponseEntity.ok(response)
    }

    /**
     * タスクを開始する
     * POST /api/tasks/{id}/start
     */
    @PostMapping("/{id}/start")
    @Operation(
        summary = "タスク開始",
        description = "指定されたIDのタスクを進行中状態に変更します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "タスクが正常に開始されました",
                content = [Content(schema = Schema(implementation = TaskResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "ビジネスルール違反",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたタスクが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun startTask(
        @Parameter(description = "タスクID", required = true)
        @PathVariable id: String,
    ): ResponseEntity<TaskResponse> {
        val taskResult = taskApplicationService.startTask(id)
        val response = TaskResponse.from(taskResult)
        return ResponseEntity.ok(response)
    }

    /**
     * タスクをキャンセルする
     * POST /api/tasks/{id}/cancel
     */
    @PostMapping("/{id}/cancel")
    @Operation(
        summary = "タスクキャンセル",
        description = "指定されたIDのタスクをキャンセル状態に変更します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "タスクが正常にキャンセルされました",
                content = [Content(schema = Schema(implementation = TaskResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "ビジネスルール違反",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたタスクが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun cancelTask(
        @Parameter(description = "タスクID", required = true)
        @PathVariable id: String,
    ): ResponseEntity<TaskResponse> {
        val taskResult = taskApplicationService.cancelTask(id)
        val response = TaskResponse.from(taskResult)
        return ResponseEntity.ok(response)
    }

    /**
     * タスクを削除する
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "タスク削除",
        description = "指定されたIDのタスクを削除します",
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "タスクが正常に削除されました",
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたタスクが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    fun deleteTask(
        @Parameter(description = "タスクID", required = true)
        @PathVariable id: String,
    ): ResponseEntity<Void> {
        taskApplicationService.deleteTask(id)
        return ResponseEntity.noContent().build()
    }
}
