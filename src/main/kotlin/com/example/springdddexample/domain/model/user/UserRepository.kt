package com.example.springdddexample.domain.model.user

/**
 * Userリポジトリインターフェース（ポート）
 * ドメイン層でデータ永続化の契約を定義
 */
interface UserRepository {
    
    /**
     * ユーザーを保存する
     * @param user 保存するユーザー
     * @return 保存されたユーザー
     */
    fun save(user: User): User
    
    /**
     * IDでユーザーを検索する
     * @param id ユーザーID
     * @return 見つかったユーザー、存在しない場合はnull
     */
    fun findById(id: UserId): User?
    
    /**
     * メールアドレスでユーザーを検索する
     * @param email メールアドレス
     * @return 見つかったユーザー、存在しない場合はnull
     */
    fun findByEmail(email: Email): User?
    
    /**
     * すべてのユーザーを取得する
     * @return ユーザーのリスト
     */
    fun findAll(): List<User>
    
    /**
     * ユーザーを削除する
     * @param id 削除するユーザーのID
     */
    fun delete(id: UserId)
    
    /**
     * 指定されたメールアドレスが既に存在するかチェックする
     * @param email チェックするメールアドレス
     * @param excludeUserId 除外するユーザーID（更新時に自分自身を除外するため）
     * @return 存在する場合true
     */
    fun existsByEmail(email: Email, excludeUserId: UserId? = null): Boolean
}