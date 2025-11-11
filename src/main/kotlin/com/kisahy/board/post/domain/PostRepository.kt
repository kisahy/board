package com.kisahy.board.post.domain

interface PostRepository {
    fun save(post: Post): Post
    fun findById(id: Long): Post?
}