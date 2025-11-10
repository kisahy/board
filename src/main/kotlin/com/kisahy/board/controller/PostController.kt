package com.kisahy.board.controller

import com.kisahy.board.domain.Post
import com.kisahy.board.service.PostService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/posts")
class PostController(
    private val postService: PostService
) {
    @PostMapping
    fun create(@RequestBody post: Post): Post = postService.create(post)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody post: Post
    ): Post = postService.update(id, post.title, post.content)
}