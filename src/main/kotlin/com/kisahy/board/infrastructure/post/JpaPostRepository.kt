package com.kisahy.board.infrastructure.post

import com.kisahy.board.domain.post.Post
import org.springframework.data.jpa.repository.JpaRepository

interface JpaPostRepository: JpaRepository<Post, Long>