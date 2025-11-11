package com.kisahy.board.global.error

open class DomainException(val errorCode: ErrorCode): RuntimeException(errorCode.message)