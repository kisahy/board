package com.kisahy.board.user.infrastructure

import com.kisahy.board.user.domain.User
import com.kisahy.board.user.domain.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaUserRepository: JpaUserRepository
): UserRepository {
    override fun save(user: User): User = jpaUserRepository.save(user)

    override fun existsByAccountId(accountId: String): Boolean = jpaUserRepository.existsByAccountId(accountId)

    override fun findById(id: Long): User? = jpaUserRepository.findById(id).orElse(null)

    override fun findByAccountId(accountId: String): User? = jpaUserRepository.findByAccountId(accountId)
}