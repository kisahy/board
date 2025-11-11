package com.kisahy.board.service

import com.kisahy.board.domain.Post
import com.kisahy.board.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun create(post: Post): Post = postRepository.save(post)

    fun update(id: Long, title: String, content: String): Post {
        val post = postRepository.findById(id).orElseThrow {
            NoSuchElementException("Post with ID $id not found")
        }

        post.title = title
        post.content = content

        return postRepository.save(post)
    }

    fun delete(id: Long): Boolean {
        val post = postRepository.findById(id).orElseThrow {
            NoSuchElementException("Post with ID $id not found")
        }

        if (post.isDeleted) {
            throw IllegalStateException("Post is deleted")
        }

        post.isDeleted = true

        postRepository.save(post)

        return true
    }
}