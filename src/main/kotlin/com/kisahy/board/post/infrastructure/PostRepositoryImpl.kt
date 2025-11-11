package com.kisahy.board.post.infrastructure

import com.kisahy.board.post.domain.Post
import com.kisahy.board.post.domain.PostRepository
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(
    private val jpaPostRepository: JpaPostRepository
): PostRepository {
    override fun save(post: Post): Post = jpaPostRepository.save(post)

    override fun findById(id: Long): Post? = jpaPostRepository.findById(id).orElse(null)
}