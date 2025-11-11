package com.kisahy.board.post.domain.exception

import com.kisahy.board.global.error.DomainException
import com.kisahy.board.global.error.ErrorCode

class PostAlreadyDeletedException: DomainException(ErrorCode.POST_ALREADY_DELETED)