package com.kisahy.board.domain.post

interface PostRepository {
    fun save(post: Post): Post
    fun findById(id: Long): Post?
}