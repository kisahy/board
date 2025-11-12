package com.kisahy.board.post.presentation.dto

import com.kisahy.board.post.domain.Post

object PostMapper {
    fun toResponse(post: Post): PostResponse = PostResponse(
        id = post.id!!,
        title = post.title,
        content = post.content,
        createdAt = post.createdAt
    )
}