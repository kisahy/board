package com.kisahy.board.post.presentation

import com.kisahy.board.post.application.PostService
import com.kisahy.board.post.presentation.dto.PostMapper
import com.kisahy.board.post.presentation.dto.PostRequest
import com.kisahy.board.post.presentation.dto.PostResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
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
    fun create(
        @Valid @RequestBody request: PostRequest
    ): ResponseEntity<PostResponse> {
        val post = postService.create(request)

        return ResponseEntity.ok(PostMapper.toResponse(post))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: PostRequest
    ): ResponseEntity<PostResponse> {
        val post = postService.update(id, request)

        return ResponseEntity.ok(PostMapper.toResponse(post))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        postService.delete(id)

        return ResponseEntity.noContent().build()
    }
}