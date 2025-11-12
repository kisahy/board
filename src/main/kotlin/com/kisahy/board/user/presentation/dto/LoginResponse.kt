package com.kisahy.board.user.presentation.dto

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)
