package com.kisahy.board.user.`interface`

import com.kisahy.board.user.application.UserService
import com.kisahy.board.user.domain.User
import com.kisahy.board.user.`interface`.dto.SignUpRequest
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
}