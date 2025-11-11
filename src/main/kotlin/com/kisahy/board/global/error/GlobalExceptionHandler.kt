package com.kisahy.board.global.error

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> =
        ResponseEntity.badRequest().body(ErrorResponse("ILLEGAL_ARGUMENT", ex.message ?: "잘못된 요청입니다."))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.allErrors.joinToString(", ") { it.defaultMessage ?: "유효하지 않은 값입니다." }
        return ResponseEntity.badRequest().body(ErrorResponse("NOT_VALID_METHOD_ARGUMENT", errors))
    }

    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ErrorResponse> {
        val code = ex.errorCode
        return ResponseEntity
            .status(code.status)
            .body(ErrorResponse(code.code, code.message))
    }
}