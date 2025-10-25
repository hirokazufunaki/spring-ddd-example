package com.example.springdddexample.presentation.rest.user

import com.example.springdddexample.application.service.user.UserApplicationService
import com.example.springdddexample.presentation.dto.user.CreateUserRequest
import com.example.springdddexample.presentation.dto.user.UpdateUserRequest
import com.example.springdddexample.presentation.dto.user.UserResponse
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
import org.springframework.web.bind.annotation.*

/**
 * ユーザーREST API コントローラー
 * ユーザーの登録、参照、更新、削除のHTTPエンドポイントを提供
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "ユーザー管理", description = "ユーザーの登録、参照、更新、削除を行うAPI")
class UserController(
    private val userApplicationService: UserApplicationService
) {
    
    /**
     * ユーザーを作成する
     * POST /api/users
     */
    @PostMapping
    @Operation(
        summary = "ユーザー作成",
        description = "新しいユーザーを作成します"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "ユーザーが正常に作成されました",
                content = [Content(schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "バリデーションエラーまたはビジネスルール違反",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun createUser(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        val userResult = userApplicationService.createUser(request.toCommand())
        val response = UserResponse.from(userResult)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
    
    /**
     * ユーザーを取得する
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "ユーザー取得",
        description = "指定されたIDのユーザーを取得します"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "ユーザーが正常に取得されました",
                content = [Content(schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたユーザーが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun getUser(
        @Parameter(description = "ユーザーID", required = true)
        @PathVariable id: String
    ): ResponseEntity<UserResponse> {
        val userResult = userApplicationService.getUser(id)
        val response = UserResponse.from(userResult)
        return ResponseEntity.ok(response)
    }
    
    /**
     * すべてのユーザーを取得する
     * GET /api/users
     */
    @GetMapping
    @Operation(
        summary = "全ユーザー取得",
        description = "登録されているすべてのユーザーを取得します"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "ユーザーリストが正常に取得されました",
                content = [Content(schema = Schema(implementation = Array<UserResponse>::class))]
            )
        ]
    )
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val userResults = userApplicationService.getAllUsers()
        val responses = userResults.map { UserResponse.from(it) }
        return ResponseEntity.ok(responses)
    }
    
    /**
     * ユーザーを更新する
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "ユーザー更新",
        description = "指定されたIDのユーザー情報を更新します"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "ユーザーが正常に更新されました",
                content = [Content(schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "バリデーションエラーまたはビジネスルール違反",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたユーザーが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun updateUser(
        @Parameter(description = "ユーザーID", required = true)
        @PathVariable id: String,
        @Valid @RequestBody request: UpdateUserRequest
    ): ResponseEntity<UserResponse> {
        val userResult = userApplicationService.updateUser(request.toCommand(id))
        val response = UserResponse.from(userResult)
        return ResponseEntity.ok(response)
    }
    
    /**
     * ユーザーを削除する
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "ユーザー削除",
        description = "指定されたIDのユーザーを削除します"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "ユーザーが正常に削除されました"
            ),
            ApiResponse(
                responseCode = "404",
                description = "指定されたユーザーが見つかりません",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun deleteUser(
        @Parameter(description = "ユーザーID", required = true)
        @PathVariable id: String
    ): ResponseEntity<Void> {
        userApplicationService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}