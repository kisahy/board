package com.kisahy.board.post.infrastructure

import com.kisahy.board.post.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPostRepository: JpaRepository<Post, Long>