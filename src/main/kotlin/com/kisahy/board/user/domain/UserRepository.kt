package com.kisahy.board.user.domain

interface UserRepository {
    fun save(user: User): User
    fun existsByLoginId(loginId: String): Boolean
    fun findById(id: Long): User?
}