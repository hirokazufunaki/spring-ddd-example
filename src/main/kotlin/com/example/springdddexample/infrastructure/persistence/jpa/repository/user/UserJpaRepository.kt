package com.example.springdddexample.infrastructure.persistence.jpa.repository.user

import com.example.springdddexample.infrastructure.persistence.jpa.entity.user.UserJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * UserJPAリポジトリ
 * Spring Data JPAによるデータアクセス
 */
@Repository
interface UserJpaRepository : JpaRepository<UserJpaEntity, String> {
    
    /**
     * メールアドレスでユーザーを検索
     */
    fun findByEmail(email: String): UserJpaEntity?
    
    /**
     * メールアドレスの存在確認
     */
    fun existsByEmail(email: String): Boolean
    
    /**
     * 指定されたIDを除外してメールアドレスの存在確認
     */
    fun existsByEmailAndIdNot(email: String, id: String): Boolean
}