package com.kisahy.board.application.post

import com.kisahy.board.domain.post.Post
import com.kisahy.board.domain.post.PostRepository
import com.kisahy.board.domain.post.exception.PostAlreadyDeletedException
import com.kisahy.board.interfaces.post.dto.PostRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostApplicationService(
    private val postRepository: PostRepository
) {
    @Transactional
    fun create(request: PostRequest): Post =
        postRepository.save(Post(
            title = request.title,
            content = request.content
        ))

    @Transactional
    fun update(id: Long, request: PostRequest): Post {
        val post = postRepository.findById(id) ?: throw IllegalStateException("게시글을 찾을 수 없습니다.")

        post.title = request.title
        post.content = request.content

        return postRepository.save(post)
    }

    @Transactional
    fun delete(id: Long) {
        val post = postRepository.findById(id) ?: throw IllegalStateException("게시글을 찾을 수 없습니다.")

        if (post.isDeleted) {
            throw PostAlreadyDeletedException()
        }

        post.isDeleted = true

        postRepository.save(post)
    }
}