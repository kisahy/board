package com.kisahy.board.user.application

import com.kisahy.board.global.security.JwtTokenProvider
import com.kisahy.board.user.domain.User
import com.kisahy.board.user.domain.UserRepository
import com.kisahy.board.user.domain.exception.UserAccountIdPolicyViolationException
import com.kisahy.board.user.domain.exception.UserInvalidLoginException
import com.kisahy.board.user.domain.exception.UserAccountIdAlreadyExistsException
import com.kisahy.board.user.domain.exception.UserPasswordMismatchException
import com.kisahy.board.user.domain.exception.UserPasswordPolicyViolationException
import com.kisahy.board.user.presentation.dto.LoginRequest
import com.kisahy.board.user.presentation.dto.LoginResponse
import com.kisahy.board.user.presentation.dto.SignUpRequest
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    private fun validateAccountIdPolicy(accountId: String) {
        val regex = Regex("^[a-z][a-z0-9_-]{2,18}[a-z0-9]$")

        if (!regex.matches(accountId)) {
            throw UserAccountIdPolicyViolationException()
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
        validateAccountIdPolicy(request.accountId)

        if (userRepository.existsByAccountId(request.accountId)) {
            throw UserAccountIdAlreadyExistsException()
        }

        if (request.password != request.confirmPassword) {
            throw UserPasswordMismatchException()
        }

        validatePasswordPolicy(request.password)

        val user = User(
            accountId = request.accountId,
            password = passwordEncoder.encode(request.password),
            name = request.name
        )

        return userRepository.save(user)
    }


    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByAccountId(request.accountId)
            ?: throw UserInvalidLoginException()

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw UserInvalidLoginException()
        }

        val accessToken = jwtTokenProvider.generateAccessToken(user.id!!)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.id!!)

        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}