package com.kisahy.board.post.domain.exception

import com.kisahy.board.global.error.DomainException
import com.kisahy.board.global.error.ErrorCode

class PostUnauthorizedAccessException(message: String? = null): DomainException(ErrorCode.POST_UNAUTHORIZED_ACCESS, message)