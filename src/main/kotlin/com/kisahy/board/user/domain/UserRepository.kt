package com.kisahy.board.user.domain

interface UserRepository {
    fun save(user: User): User
    fun existsByAccountId(accountId: String): Boolean
    fun findById(id: Long): User?
    fun findByAccountId(accountId: String): User?
}