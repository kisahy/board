package com.kisahy.board.interfaces.advice

import com.kisahy.board.domain.post.exception.PostAlreadyDeletedException
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException) = ResponseEntity.badRequest().body(mapOf("error" to ex.message.orEmpty()))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String>?> {
        val errors = ex.bindingResult.allErrors.joinToString(", ") { it.defaultMessage ?: "Invalid" }
        return ResponseEntity.badRequest().body(mapOf("error" to errors))
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(ex: IllegalStateException): ResponseEntity<Map<String, String>?> {
        val body = mapOf("error" to ex.message.orEmpty())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body)
    }

    @ExceptionHandler(PostAlreadyDeletedException::class)
    fun handlePostAlreadyDeletedException(ex: PostAlreadyDeletedException) = ResponseEntity.badRequest().body(mapOf("error" to ex.message.orEmpty()))
}