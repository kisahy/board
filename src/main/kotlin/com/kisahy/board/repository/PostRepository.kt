package com.kisahy.board.repository

import com.kisahy.board.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long>