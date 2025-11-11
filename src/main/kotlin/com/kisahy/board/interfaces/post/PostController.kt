package com.kisahy.board.interfaces.post

import com.kisahy.board.domain.post.Post
import com.kisahy.board.application.post.PostApplicationService
import com.kisahy.board.interfaces.post.dto.PostRequest
import com.kisahy.board.interfaces.post.dto.PostMapper
import com.kisahy.board.interfaces.post.dto.PostResponse
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
    private val postApplicationService: PostApplicationService
) {
    @PostMapping
    fun create(
        @Valid @RequestBody request: PostRequest
    ): ResponseEntity<PostResponse> {
        val post = postApplicationService.create(request)

        return ResponseEntity.ok(PostMapper.toResponse(post))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: PostRequest
    ): ResponseEntity<PostResponse> {
        val post = postApplicationService.update(id, request)

        return ResponseEntity.ok(PostMapper.toResponse(post))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        postApplicationService.delete(id)

        return ResponseEntity.noContent().build()
    }
}