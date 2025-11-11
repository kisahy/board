package com.kisahy.board.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.kisahy.board.post.application.PostService
import com.kisahy.board.post.domain.Post
import com.kisahy.board.post.infrastructure.JpaPostRepository
import com.kisahy.board.post.`interface`.dto.PostRequest
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.assertTrue

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var postService: PostService

    @Autowired
    lateinit var postRepository: JpaPostRepository

    fun seed(): Post {
        val request = PostRequest(title = "Title", content = "Content")

        return postService.create(request)
    }

    @Test
    fun `게시글 생성 validation 실패 - 제목 없음`() {
        val request = PostRequest(title = "", content = "Content")

        val response = mockMvc.perform(
            post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("NOT_VALID_METHOD_ARGUMENT"))
            .andExpect(jsonPath("$.message").value("게시글의 제목을 입력해주세요."))
    }

    @Test
    fun `게시글 생성 validation 실패 - 내용 없음`() {
        val request = PostRequest(title = "Title", content = "")

        mockMvc.perform(
            post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("NOT_VALID_METHOD_ARGUMENT"))
            .andExpect(jsonPath("$.message").value("게시글의 내용을 입력해주세요."))
    }

    @Test
    fun `게시글 생성 성공`() {
        val request = PostRequest(title = "Title", content = "Content")

        val result = mockMvc.perform(
            post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Title"))
            .andExpect(jsonPath("$.content").value("Content"))
            .andReturn()

        val responseBody = result.response.contentAsString
        val createdPost = objectMapper.readValue(responseBody, Post::class.java)

        val savedPost = postRepository.findById(createdPost.id!!).orElseThrow()

        Assertions.assertEquals("Title", savedPost.title)
        Assertions.assertEquals("Content", savedPost.content)
    }

    @Test
    fun `게시글 수정 validation 실패 - 제목 없음`() {
        val post = seed()
        val request = PostRequest(title = "", content = "Content")

        val response = mockMvc.perform(
            put("/api/posts/${post.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("NOT_VALID_METHOD_ARGUMENT"))
            .andExpect(jsonPath("$.message").value("게시글의 제목을 입력해주세요."))
    }

    @Test
    fun `게시글 수정 validation 실패 - 내용 없음`() {
        val post = seed()
        val request = PostRequest(title = "Title", content = "")

        mockMvc.perform(
            put("/api/posts/${post.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("NOT_VALID_METHOD_ARGUMENT"))
            .andExpect(jsonPath("$.message").value("게시글의 내용을 입력해주세요."))
    }

    @Test
    fun `게시글 수정 validation 실패 - 게시글 없음`() {
        val request = PostRequest(title = "Title", content = "Content")

        val response = mockMvc.perform(
            put("/api/posts/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("POST_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
    }

    @Test
    fun `게시글 수정 성공`() {
        val post = seed()
        val request = PostRequest(title = "Update Title", content = "Update Content")

        val result = mockMvc.perform(
            put("/api/posts/${post.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Update Title"))
            .andExpect(jsonPath("$.content").value("Update Content"))
            .andReturn()

        val responseBody = result.response.contentAsString
        val updatedPost = objectMapper.readValue(responseBody, Post::class.java)

        val savedPost = postRepository.findById(updatedPost.id!!).orElseThrow()

        Assertions.assertEquals("Update Title", savedPost.title)
        Assertions.assertEquals("Update Content", savedPost.content)
    }

    @Test
    fun `게시글 삭제 validation 실패 - 게시글 없음`() {
        mockMvc.perform(
            delete("/api/posts/999")
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("POST_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
    }

    @Test
    fun `게시글 삭제 validation 실패 - 이미 삭제된 게시글`() {
        val post = seed()

        postService.delete(post.id!!)

        mockMvc.perform(
            delete("/api/posts/${post.id}")
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value("POST_ALREADY_DELETED"))
            .andExpect(jsonPath("$.message").value("이미 삭제된 게시글입니다."))
    }

    @Test
    fun `게시글 삭제 성공`() {
        val post = seed()

        val result = mockMvc.perform(
            delete("/api/posts/${post.id}")
        )
            .andExpect(status().isNoContent)

        val deletedPost = postRepository.findById(post.id!!).get()

        assertTrue(deletedPost.isDeleted)
    }
}