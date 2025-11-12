package com.kisahy.board.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.kisahy.board.global.security.JwtTokenProvider
import com.kisahy.board.post.domain.Post
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerUpdateTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    lateinit var postRepository: PostRepository

    @Autowired
    lateinit var userRepository: UserRepository

    fun seedUser(accountId: String): User =
        userRepository.save(User(
            accountId = accountId,
            password = "kisahy123!@#$",
            name = "상현"
        ))

    fun seedPost(): Post {
        return postRepository.save(Post(
            title = "Title",
            content = "Content",
            user = seedUser("kisahy")
        ))
    }

    @Test
    fun `게시글 수정 실패 - 비로그인`() {
        val post = seedPost()
        val request = PostRequest(title = "Title", content = "Content")

        mockMvc.perform(
            put("/api/posts/${post.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `게시글 수정 실패 - 다른 작성자`() {
        val otherUser = seedUser("otherUser")
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(otherUser.id!!)
        val request = PostRequest(title = "Title", content = "Content")

        mockMvc.perform(
            put("/api/posts/${post.id}")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.code").value("POST_UNAUTHORIZED_ACCESS"))
    }

    @Test
    fun `게시글 수정 validation 실패 - 제목 없음`() {
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(post.user.id!!)
        val request = PostRequest(title = "", content = "Content")

        mockMvc.perform(
            put("/api/posts/${post.id}")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("NOT_VALID_METHOD_ARGUMENT"))
    }

    @Test
    fun `게시글 수정 validation 실패 - 내용 없음`() {
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(post.user.id!!)
        val request = PostRequest(title = "Title", content = "")

        mockMvc.perform(
            put("/api/posts/${post.id}")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("NOT_VALID_METHOD_ARGUMENT"))
            .andExpect(jsonPath("$.message").value("게시글의 내용을 입력해주세요."))
    }

    @Test
    fun `게시글 수정 validation 실패 - 게시글 없음`() {
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(post.user.id!!)
        val request = PostRequest(title = "Title", content = "Content")

        val response = mockMvc.perform(
            put("/api/posts/999")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("POST_NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("게시글을 찾을 수 없습니다."))
    }

    @Test
    fun `게시글 수정 성공`() {
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(post.user.id!!)
        val request = PostRequest(title = "Update Title", content = "Update Content")

        val result = mockMvc.perform(
            put("/api/posts/${post.id}")
                .header("Authorization", "Bearer $accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.title").value("Update Title"))
            .andExpect(jsonPath("$.content").value("Update Content"))
            .andReturn()

        val responseBody = result.response.contentAsString
        val updatedPost = objectMapper.readValue(responseBody, PostResponse::class.java)

        val savedPost = postRepository.findById(updatedPost.id!!)

        Assertions.assertEquals(updatedPost.title, savedPost!!.title)
        Assertions.assertEquals(updatedPost.content, savedPost.content)
        Assertions.assertEquals(updatedPost.userId, savedPost.user.id)
        Assertions.assertEquals(updatedPost.userName, savedPost.user.name)
    }
}