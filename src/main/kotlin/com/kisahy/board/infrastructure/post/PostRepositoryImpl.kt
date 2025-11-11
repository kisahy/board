package com.kisahy.board.infrastructure.post

import com.kisahy.board.domain.post.Post
import com.kisahy.board.domain.post.PostRepository
import org.springframework.stereotype.Repository

@Repository
class PostRepositoryImpl(
    private val jpaPostRepository: JpaPostRepository
): PostRepository {
    override fun save(post: Post): Post = jpaPostRepository.save(post)

    override fun findById(id: Long): Post? = jpaPostRepository.findById(id).orElse(null)
}