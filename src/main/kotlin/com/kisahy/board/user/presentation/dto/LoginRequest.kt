package com.kisahy.board.user.presentation.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "아이디를 입력해주세요.")
    val accountId: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String
)
