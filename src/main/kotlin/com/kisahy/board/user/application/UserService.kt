package com.kisahy.board.user.application

import com.kisahy.board.user.domain.User
import com.kisahy.board.user.domain.UserRepository
import com.kisahy.board.user.domain.exception.UserLoginIdAlreadyExistsException
import com.kisahy.board.user.`interface`.dto.SignUpRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun signUp(request: SignUpRequest): User {
        if (userRepository.existsByLoginId(request.loginId)) {
            throw UserLoginIdAlreadyExistsException()
        }

        val user = User(
            loginId = request.loginId,
            password = request.password,
            name = request.name
        )

        return userRepository.save(user)
    }
}