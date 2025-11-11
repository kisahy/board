package com.kisahy.board.global.error

import org.springframework.http.HttpStatus

enum class ErrorCode(val code: String, val status: HttpStatus, val message: String) {
    //post
    POST_NOT_FOUND("POST_NOT_FOUND", HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    POST_ALREADY_DELETED("POST_ALREADY_DELETED", HttpStatus.CONFLICT, "이미 삭제된 게시글입니다."),

    //user
    USER_LOGIN_ID_ALREADY_EXISTS("USER_LOGIN_ID_ALREADY_EXISTS", HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    USER_PASSWORD_MISMATCH("USER_PASSWORD_MISMATCH", HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    USER_PASSWORD_POLICY_VIOLATION("USER_PASSWORD_POLICY_VIOLATION", HttpStatus.BAD_REQUEST, "비밀번호는 영어, 숫자, 특수문자를 포함한 8~20자여야 합니다."),
    USER_ACCOUNT_ID_POLICY_VIOLATION("USER_ACCOUNT_ID_POLICY_VIOLATION", HttpStatus.BAD_REQUEST, "아이디는 영어 소문자로 시작하고, 숫자/_, -를 포함할 수 있으나 마지막 글자는 특수문자를 사용할 수 없습니다. 길이는 4~20자여야 합니다."),
}