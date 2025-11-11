package com.kisahy.board.post.application

import com.kisahy.board.post.domain.Post
import com.kisahy.board.post.domain.PostRepository
import com.kisahy.board.post.domain.exception.PostAlreadyDeletedException
import com.kisahy.board.post.domain.exception.PostNotFoundException
import com.kisahy.board.post.`interface`.dto.PostRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository
) {
    @Transactional
    fun create(request: PostRequest): Post =
        postRepository.save(
            Post(
                title = request.title,
                content = request.content
            )
        )

    @Transactional
    fun update(id: Long, request: PostRequest): Post {
        val post = postRepository.findById(id) ?: throw PostNotFoundException()

        post.title = request.title
        post.content = request.content

        return postRepository.save(post)
    }

    @Transactional
    fun delete(id: Long) {
        val post = postRepository.findById(id) ?: throw PostNotFoundException()

        if (post.isDeleted) {
            throw PostAlreadyDeletedException()
        }

        post.isDeleted = true

        postRepository.save(post)
    }
}