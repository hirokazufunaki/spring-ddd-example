package com.example.springdddexample.infrastructure.persistence.jpa.adapter.user

import com.example.springdddexample.domain.model.user.User
import com.example.springdddexample.domain.model.user.UserId
import com.example.springdddexample.domain.model.user.UserRepository
import com.example.springdddexample.infrastructure.persistence.jpa.mapper.user.UserMapper
import com.example.springdddexample.infrastructure.persistence.jpa.repository.user.UserJpaRepository
import org.springframework.stereotype.Repository

/**
 * ユーザーリポジトリアダプター
 * ドメインのUserRepositoryインターフェースの実装
 */
@Repository
class UserRepositoryAdapter(
    private val userJpaRepository: UserJpaRepository,
    private val userMapper: UserMapper,
) : UserRepository {
    override fun save(user: User): User {
        val entity = userMapper.toJpaEntity(user)
        val savedEntity = userJpaRepository.save(entity)
        return userMapper.toDomainObject(savedEntity)
    }

    override fun findById(id: UserId): User? =
        userJpaRepository
            .findById(id.value)
            .map { userMapper.toDomainObject(it) }
            .orElse(null)

    override fun findByEmail(email: Email): User? =
        userJpaRepository
            .findByEmail(email.value)
            ?.let { userMapper.toDomainObject(it) }

    override fun findAll(): List<User> {
        val entities = userJpaRepository.findAll()
        return userMapper.toDomainObjects(entities)
    }

    override fun delete(id: UserId) {
        userJpaRepository.deleteById(id.value)
    }

    override fun existsByEmail(
        email: Email,
        excludeUserId: UserId?,
    ): Boolean =
        if (excludeUserId != null) {
            userJpaRepository.existsByEmailAndIdNot(email.value, excludeUserId.value)
        } else {
            userJpaRepository.existsByEmail(email.value)
        }
}
