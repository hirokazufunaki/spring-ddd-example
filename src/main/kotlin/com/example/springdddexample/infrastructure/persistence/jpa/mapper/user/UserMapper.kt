package com.example.springdddexample.infrastructure.persistence.jpa.mapper.user

import com.example.springdddexample.domain.model.user.Email
import com.example.springdddexample.domain.model.user.User
import com.example.springdddexample.domain.model.user.UserId
import com.example.springdddexample.domain.model.user.UserName
import com.example.springdddexample.infrastructure.persistence.jpa.entity.user.UserJpaEntity
import org.springframework.stereotype.Component

/**
 * ドメインオブジェクトとJPAエンティティ間の変換を行うマッパー
 */
@Component
class UserMapper {
    /**
     * ドメインオブジェクトからJPAエンティティに変換
     */
    fun toJpaEntity(user: User): UserJpaEntity =
        UserJpaEntity(
            id = user.id.value,
            name = user.name.value,
            email = user.email.value,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt,
        )

    /**
     * JPAエンティティからドメインオブジェクトに変換
     */
    fun toDomainObject(entity: UserJpaEntity): User =
        User(
            id = UserId(entity.id),
            name = UserName(entity.name),
            email = Email(entity.email),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    /**
     * JPAエンティティのリストからドメインオブジェクトのリストに変換
     */
    fun toDomainObjects(entities: List<UserJpaEntity>): List<User> = entities.map { toDomainObject(it) }
}
