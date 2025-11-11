package com.kisahy.board.user.`interface`.dto

import jakarta.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank(message = "아이디를 입력해주세요.")
    val loginId: String,

    @field:NotBlank(message = "이름을 입력해주세요.")
    val name: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String,

    @field:NotBlank(message = "비밀번호 확인을 입력해주세요.")
    val confirmPassword: String
)
