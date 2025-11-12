package com.kisahy.board.global.error

open class DomainException(val errorCode: ErrorCode, message: String? = null): RuntimeException(message ?: errorCode.message)