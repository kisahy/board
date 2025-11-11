package com.kisahy.board.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.kisahy.board.user.application.UserService
import com.kisahy.board.user.domain.User
import com.kisahy.board.user.domain.UserRepository
import com.kisahy.board.user.`interface`.dto.SignUpRequest
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
class UserControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    fun seed(): User {
        val request = SignUpRequest(
            loginId = "kisahy",
            password = "kisahy",
            confirmPassword = "kisahy",
            name = "kisahy",
        )

        return userService.signUp(request)
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 아이디 규칙(최소글자)`() {
        val request = SignUpRequest(
            loginId = "kis",
            password = "kisahy",
            confirmPassword = "kisahy",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_ACCOUNT_ID_POLICY_VIOLATION"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 아이디 규칙(첫글자숫자)`() {
        val request = SignUpRequest(
            loginId = "9kisahy",
            password = "kisahy",
            confirmPassword = "kisahy",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_ACCOUNT_ID_POLICY_VIOLATION"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 아이디 규칙(마지막 글자 특수문자)`() {
        val request = SignUpRequest(
            loginId = "kisahy_",
            password = "kisahy",
            confirmPassword = "kisahy",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_ACCOUNT_ID_POLICY_VIOLATION"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 아이디 중복`() {
        seed()

        val request = SignUpRequest(
            loginId = "kisahy",
            password = "kisahy",
            confirmPassword = "kisahy",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.code").value("USER_LOGIN_ID_ALREADY_EXISTS"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 비밀번호 규칙(최소글자)`() {
        val request = SignUpRequest(
            loginId = "kisahy",
            password = "k8!",
            confirmPassword = "k8!",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_PASSWORD_POLICY_VIOLATION"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 비밀번호 규칙(최대글자)`() {
        val request = SignUpRequest(
            loginId = "kisahy",
            password = "abcdefg1234567!@#$%^&",
            confirmPassword = "abcdefg1234567!@#$%^&",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_PASSWORD_POLICY_VIOLATION"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 비밀번호 규칙(문자 누락)`() {
        val request = SignUpRequest(
            loginId = "kisahy",
            password = "1234567!@#$%^&",
            confirmPassword = "1234567!@#$%^&",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_PASSWORD_POLICY_VIOLATION"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 비밀번호 규칙(숫자 누락)`() {
        val request = SignUpRequest(
            loginId = "kisahy",
            password = "abcdefg!@#$%^&",
            confirmPassword = "abcdefg!@#$%^&",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_PASSWORD_POLICY_VIOLATION"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 비밀번호 규칙(특수문자 누락)`() {
        val request = SignUpRequest(
            loginId = "kisahy",
            password = "abcdefg1234567",
            confirmPassword = "abcdefg1234567",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_PASSWORD_POLICY_VIOLATION"))
    }

    @Test
    fun `사용자 회원가입 validation 실패 - 비밀번호 불일치`() {
        val request = SignUpRequest(
            loginId = "kisahy",
            password = "abcd1234!@#$",
            confirmPassword = "abc123!@#",
            name = "kisahy",
        )

        mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("USER_PASSWORD_MISMATCH"))
    }

    @Test
    fun `게시글 생성 성공`() {
        val request = SignUpRequest(
            loginId = "kisahy",
            password = "abcd1234!@#$",
            confirmPassword = "abcd1234!@#$",
            name = "kisahy",
        )

        val result = mockMvc.perform(
            post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.loginId").value("kisahy"))
            .andExpect(jsonPath("$.password").value("abcd1234!@#$"))
            .andExpect(jsonPath("$.name").value("kisahy"))
            .andReturn()

        val responseBody = result.response.contentAsString
        val signUpUser = objectMapper.readValue(responseBody, User::class.java)

        val user = userRepository.findById(signUpUser.id!!)

        Assertions.assertEquals("kisahy", user!!.loginId)
        Assertions.assertEquals("kisahy", user.name)
        Assertions.assertEquals("abcd1234!@#$", user.password)
    }
}