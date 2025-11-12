package com.kisahy.board.user.infrastructure

import com.kisahy.board.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository: JpaRepository<User, Long> {
    fun existsByAccountId(accountId: String): Boolean
    fun findByAccountId(accountId: String): User?
}