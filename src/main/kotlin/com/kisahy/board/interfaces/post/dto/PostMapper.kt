package com.kisahy.board.interfaces.post.dto

import com.kisahy.board.domain.post.Post

object PostMapper {
    fun toResponse(post: Post): PostResponse = PostResponse(
        id = post.id!!,
        title = post.title,
        content = post.content,
        createdAt = post.createdAt
    )
}