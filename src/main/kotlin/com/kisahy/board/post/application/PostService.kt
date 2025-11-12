package com.kisahy.board.post.application

import com.kisahy.board.post.domain.Post
import com.kisahy.board.post.domain.PostRepository
import com.kisahy.board.post.domain.exception.PostAlreadyDeletedException
import com.kisahy.board.post.domain.exception.PostNotFoundException
import com.kisahy.board.post.domain.exception.PostUnauthorizedAccessException
import com.kisahy.board.post.presentation.dto.PostRequest
import com.kisahy.board.post.presentation.dto.PostResponse
import com.kisahy.board.user.domain.UserRepository
import com.kisahy.board.user.domain.exception.UserNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun create(request: PostRequest, userId: Long): Post {
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException()

        return postRepository.save(
            Post(
                title = request.title,
                content = request.content,
                user = user
            )
        )
    }

    @Transactional
    fun update(postId: Long, request: PostRequest, userId: Long): Post {
        val post = postRepository.findById(postId) ?: throw PostNotFoundException()

        if (post.user.id != userId) {
            throw PostUnauthorizedAccessException("본인이 작성한 게시글만 수정할 수 있습니다.")
        }

        post.title = request.title
        post.content = request.content

        return postRepository.save(post)
    }

    @Transactional
    fun delete(postId: Long, userId: Long) {
        val post = postRepository.findById(postId) ?: throw PostNotFoundException()

        if (post.isDeleted) {
            throw PostAlreadyDeletedException()
        }

        if (post.user.id != userId) {
            throw PostUnauthorizedAccessException("본인이 작성한 게시글만 삭제할 수 있습니다.")
        }

        post.isDeleted = true

        postRepository.save(post)
    }
}