package com.example.springdddexample.application.service.user

import com.example.springdddexample.application.dto.user.CreateUserInput
import com.example.springdddexample.application.dto.user.UpdateUserInput
import com.example.springdddexample.application.dto.user.UserOutput
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
    fun createUser(input: CreateUserInput): UserOutput {
        val email = Email(input.email)

        // メールアドレスの重複チェック
        if (userRepository.existsByEmail(email)) {
            throw BusinessRuleViolationException("このメールアドレスは既に使用されています: ${input.email}")
        }

        val user =
            User.create(
                name = UserName(input.name),
                email = email,
            )

        val savedUser = userRepository.save(user)
        return UserOutput.from(savedUser)
    }

    /**
     * ユーザーを更新する
     */
    fun updateUser(input: UpdateUserInput): UserOutput {
        val userId = UserId(input.id)
        val existingUser =
            userRepository.findById(userId)
                ?: throw NotFoundException("ユーザーが見つかりません: ${input.id}")

        val email = Email(input.email)

        // メールアドレスの重複チェック（自分以外）
        if (userRepository.existsByEmail(email, userId)) {
            throw BusinessRuleViolationException("このメールアドレスは既に使用されています: ${input.email}")
        }

        val updatedUser =
            existingUser.updateProfile(
                newName = UserName(input.name),
                newEmail = email,
            )

        val savedUser = userRepository.save(updatedUser)
        return UserOutput.from(savedUser)
    }

    /**
     * ユーザーを取得する
     */
    @Transactional(readOnly = true)
    fun getUser(id: String): UserOutput {
        val userId = UserId(id)
        val user =
            userRepository.findById(userId)
                ?: throw NotFoundException("ユーザーが見つかりません: $id")

        return UserOutput.from(user)
    }

    /**
     * すべてのユーザーを取得する
     */
    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserOutput> =
        userRepository
            .findAll()
            .map { UserOutput.from(it) }

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
