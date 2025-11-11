package com.kisahy.board.user.infrastructure

import com.kisahy.board.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface JpaUserRepository: JpaRepository<User, Long> {
    fun existsByLoginId(loginId: String): Boolean
}