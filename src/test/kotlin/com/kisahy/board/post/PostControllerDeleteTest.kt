package com.kisahy.board.post

import com.kisahy.board.global.security.JwtTokenProvider
import com.kisahy.board.post.application.PostService
import com.kisahy.board.post.domain.Post
import com.kisahy.board.post.domain.PostRepository
import com.kisahy.board.post.presentation.dto.PostRequest
import com.kisahy.board.user.domain.User
import com.kisahy.board.user.domain.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerDeleteTest {
    @Autowired
    lateinit var mockMvc: MockMvc

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
    fun `게시글 삭제 실패 - 비로그인`() {
        val post = seedPost()

        mockMvc.perform(delete("/api/posts/${post.id}"))
            .andExpect(status().isForbidden)
    }

    @Test
    fun `게시글 삭제 실패 - 다른 작성자`() {
        val otherUser = seedUser("otherUser")
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(otherUser.id!!)

        mockMvc.perform(
            delete("/api/posts/${post.id}")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.code").value("POST_UNAUTHORIZED_ACCESS"))
    }

    @Test
    fun `게시글 삭제 validation 실패 - 게시글 없음`() {
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(post.user.id!!)

        mockMvc.perform(
            delete("/api/posts/999")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.code").value("POST_NOT_FOUND"))
    }

    @Test
    fun `게시글 삭제 validation 실패 - 이미 삭제된 게시글`() {
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(post.user.id!!)

        post.isDeleted = true
        postRepository.save(post)

        mockMvc.perform(
            delete("/api/posts/${post.id}")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value("POST_ALREADY_DELETED"))
    }

    @Test
    fun `게시글 삭제 성공`() {
        val post = seedPost()
        val accessToken = jwtTokenProvider.generateAccessToken(post.user.id!!)

        mockMvc.perform(
            delete("/api/posts/${post.id}")
                .header("Authorization", "Bearer $accessToken")
        )
            .andExpect(status().isNoContent)

        val deletedPost = postRepository.findById(post.id!!)

        assertTrue(deletedPost!!.isDeleted)
    }
}