package com.example.springdddexample.application.service.user

import com.example.springdddexample.application.dto.user.CreateUserCommand
import com.example.springdddexample.application.dto.user.UpdateUserCommand
import com.example.springdddexample.application.dto.user.UserResult
import com.example.springdddexample.domain.model.user.Email
import com.example.springdddexample.domain.model.user.User
import com.example.springdddexample.domain.model.user.UserId
import com.example.springdddexample.domain.model.user.UserName
import com.example.springdddexample.domain.model.user.UserRepository
import com.example.springdddexample.domain.shared.BusinessRuleViolationException
import com.example.springdddexample.domain.shared.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * ユーザーアプリケーションサービス
 * ユーザーに関するユースケースを統制する
 */
@Service
@Transactional
class UserApplicationService(
    private val userRepository: UserRepository,
) {
    /**
     * ユーザーを作成する
     */
    fun createUser(command: CreateUserCommand): UserResult {
        val email = Email(command.email)

        // メールアドレスの重複チェック
        if (userRepository.existsByEmail(email)) {
            throw BusinessRuleViolationException("このメールアドレスは既に使用されています: ${command.email}")
        }

        val user =
            User.create(
                name = UserName(command.name),
                email = email,
            )

        val savedUser = userRepository.save(user)
        return UserResult.from(savedUser)
    }

    /**
     * ユーザーを更新する
     */
    fun updateUser(command: UpdateUserCommand): UserResult {
        val userId = UserId(command.id)
        val existingUser =
            userRepository.findById(userId)
                ?: throw NotFoundException("ユーザーが見つかりません: ${command.id}")

        val email = Email(command.email)

        // メールアドレスの重複チェック（自分以外）
        if (userRepository.existsByEmail(email, userId)) {
            throw BusinessRuleViolationException("このメールアドレスは既に使用されています: ${command.email}")
        }

        val updatedUser =
            existingUser.updateProfile(
                newName = UserName(command.name),
                newEmail = email,
            )

        val savedUser = userRepository.save(updatedUser)
        return UserResult.from(savedUser)
    }

    /**
     * ユーザーを取得する
     */
    @Transactional(readOnly = true)
    fun getUser(id: String): UserResult {
        val userId = UserId(id)
        val user =
            userRepository.findById(userId)
                ?: throw NotFoundException("ユーザーが見つかりません: $id")

        return UserResult.from(user)
    }

    /**
     * すべてのユーザーを取得する
     */
    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserResult> =
        userRepository
            .findAll()
            .map { UserResult.from(it) }

    /**
     * ユーザーを削除する
     */
    fun deleteUser(id: String) {
        val userId = UserId(id)

        // ユーザーの存在確認
        userRepository.findById(userId)
            ?: throw NotFoundException("ユーザーが見つかりません: $id")

        userRepository.delete(userId)
    }
}
