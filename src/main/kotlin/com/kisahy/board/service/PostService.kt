package com.kisahy.board.service

import com.kisahy.board.domain.Post
import com.kisahy.board.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun create(post: Post): Post = postRepository.save(post)
}