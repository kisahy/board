package com.kisahy.board.global.error

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: String, val status: HttpStatus, val message: String) {
    //post
    POST_NOT_FOUND("POST_NOT_FOUND", HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    POST_ALREADY_DELETED("POST_ALREADY_DELETED", HttpStatus.CONFLICT, "이미 삭제된 게시글입니다."),

    //user
    USER_LOGIN_ID_ALREADY_EXISTS("USER_LOGIN_ID_ALREADY_EXISTS", HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
}