package com.kisahy.board.user.application

import com.kisahy.board.user.domain.User
import com.kisahy.board.user.domain.UserRepository
import com.kisahy.board.user.domain.exception.UserLoginIdAlreadyExistsException
import com.kisahy.board.user.domain.exception.UserLoginIdPolicyViolationException
import com.kisahy.board.user.domain.exception.UserPasswordMismatchException
import com.kisahy.board.user.domain.exception.UserPasswordPolicyViolationException
import com.kisahy.board.user.`interface`.dto.SignUpRequest
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private fun validateLoginIdPolicy(loginId: String) {
        val regex = Regex("^[a-z][a-z0-9_-]{2,18}[a-z0-9]$")

        if (!regex.matches(loginId)) {
            throw UserLoginIdPolicyViolationException()
        }
    }

    private fun validatePasswordPolicy(password: String) {
        val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/]).{8,20}\$")

        if (!regex.matches(password)) {
            throw UserPasswordPolicyViolationException()
        }
    }

    @Transactional
    fun signUp(request: SignUpRequest): User {
        validateLoginIdPolicy(request.loginId)

        if (userRepository.existsByLoginId(request.loginId)) {
            throw UserLoginIdAlreadyExistsException()
        }

        if (request.password != request.confirmPassword) {
            throw UserPasswordMismatchException()
        }

        validatePasswordPolicy(request.password)

        val user = User(
            loginId = request.loginId,
            password = passwordEncoder.encode(request.password),
            name = request.name
        )

        return userRepository.save(user)
    }
}