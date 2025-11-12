package com.kisahy.board.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.kisahy.board.global.security.JwtTokenProvider
import com.kisahy.board.post.domain.PostRepository
import com.kisahy.board.post.presentation.dto.PostRequest
import com.kisahy.board.post.presentation.dto.PostResponse
import com.kisahy.board.user.domain.User
import com.kisahy.board.user.domain.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
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
class PostControllerCreateTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    fun seedUser(): User = userRepository.save(User(
            accountId = "kisahy",
            password = "kisahy123!@#$",
            name = "상현"
        ))

    @Test
    fun `게시글 생성 실패 - 비로그인`() {
        val request = PostRequest(title = "", content = "Content")

        mockMvc.perform(
            post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `게시글 생성 validation 실패 - 제목 없음`() {
        val user = seedUser()
        val accessToken = jwtTokenProvider.generateAccessToken(user.id!!)
        val request = PostRequest(title = "", content = "Content")

        mockMvc.perform(
            post("/api/posts")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("NOT_VALID_METHOD_ARGUMENT"))
            .andExpect(jsonPath("$.message").value("게시글의 제목을 입력해주세요."))
    }

    @Test
    fun `게시글 생성 validation 실패 - 내용 없음`() {
        val user = seedUser()
        val accessToken = jwtTokenProvider.generateAccessToken(user.id!!)
        val request = PostRequest(title = "Title", content = "")

        mockMvc.perform(
            post("/api/posts")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("NOT_VALID_METHOD_ARGUMENT"))
            .andExpect(jsonPath("$.message").value("게시글의 내용을 입력해주세요."))
    }

    @Test
    fun `게시글 생성 성공`() {
        val user = seedUser()
        val accessToken = jwtTokenProvider.generateAccessToken(user.id!!)
        val request = PostRequest(title = "Title", content = "Content")

        val result = mockMvc.perform(
            post("/api/posts")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Title"))
            .andExpect(jsonPath("$.content").value("Content"))
            .andReturn()

        val responseBody = result.response.contentAsString
        val createdPost = objectMapper.readValue(responseBody, PostResponse::class.java)

        val savedPost = postRepository.findById(createdPost.id)

        Assertions.assertEquals(createdPost.title, savedPost!!.title)
        Assertions.assertEquals(createdPost.content, savedPost.content)
        Assertions.assertEquals(createdPost.userId, savedPost.user.id)
        Assertions.assertEquals(createdPost.userName, savedPost.user.name)
    }
}