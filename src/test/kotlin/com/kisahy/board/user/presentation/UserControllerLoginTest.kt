package com.kisahy.board.user.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.kisahy.board.global.security.JwtTokenProvider
import com.kisahy.board.user.application.UserService
import com.kisahy.board.user.domain.User
import com.kisahy.board.user.presentation.dto.LoginRequest
import com.kisahy.board.user.presentation.dto.LoginResponse
import com.kisahy.board.user.presentation.dto.SignUpRequest
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerLoginTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    fun seed(): User {
        val request = SignUpRequest(
            accountId = "kisahy",
            password = "abcd1234!@",
            confirmPassword = "abcd1234!@",
            name = "kisahy",
        )

        return userService.signUp(request)
    }

    @Test
    fun `로그인 실패 - 아이디 오류`() {
        seed()

        val request = LoginRequest(
            accountId = "kisahyy",
            password = "abcd1234!@"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("USER_INVALID_LOGIN"))
    }

    @Test
    fun `로그인 실패 - 비밀번호 오류`() {
        seed()

        val request = LoginRequest(
            accountId = "kisahy",
            password = "abcd1234!@#"
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("USER_INVALID_LOGIN"))
    }

    @Test
    fun `사용자 회원가입 성공`() {
        seed()

        val request = LoginRequest(
            accountId = "kisahy",
            password = "abcd1234!@"
        )

        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists())
            .andReturn()

        val responseBody = result.response.contentAsString
        val response = objectMapper.readValue(responseBody, LoginResponse::class.java)


        Assertions.assertTrue(jwtTokenProvider.validateAccessToken(response.accessToken))
        Assertions.assertTrue(jwtTokenProvider.validateRefreshToken(response.refreshToken))
    }
}