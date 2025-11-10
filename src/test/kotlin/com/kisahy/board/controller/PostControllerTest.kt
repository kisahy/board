package com.kisahy.board.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.kisahy.board.domain.Post
import com.kisahy.board.repository.PostRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var postRepository: PostRepository

    @Test
    fun `게시글 생성 API 테스트`() {
        val post = Post(
            title = "Test title",
            content = "Test content"
        )

        val result = mockMvc.perform(
            post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Test title"))
            .andExpect(jsonPath("$.content").value("Test content"))
            .andReturn()

        val responseBody = result.response.contentAsString
        val createdPost = objectMapper.readValue(responseBody, Post::class.java)

        val savedPost = postRepository.findById(createdPost.id!!).orElseThrow()

        assertEquals("Test title", savedPost.title)
        assertEquals("Test content", savedPost.content)
    }
}