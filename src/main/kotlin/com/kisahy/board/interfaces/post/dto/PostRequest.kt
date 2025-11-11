package com.kisahy.board.interfaces.post.dto

import jakarta.validation.constraints.NotBlank

data class PostRequest(
    @field:NotBlank(message = "게시글의 제목을 입력해주세요.")
    val title: String,

    @field:NotBlank(message = "게시글의 내용을 입력해주세요.")
    val content: String
)
