package com.kisahy.board.user.presentation

import com.kisahy.board.user.application.UserService
import com.kisahy.board.user.domain.User
import com.kisahy.board.user.presentation.dto.LoginRequest
import com.kisahy.board.user.presentation.dto.LoginResponse
import com.kisahy.board.user.presentation.dto.SignUpRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignUpRequest): ResponseEntity<User> {
        val user = userService.signUp(request)
        return ResponseEntity.ok(user)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = userService.login(request)
        return ResponseEntity.ok(response)
    }
}