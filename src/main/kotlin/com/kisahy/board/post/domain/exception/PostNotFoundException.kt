package com.kisahy.board.post.domain.exception

import com.kisahy.board.global.error.DomainException
import com.kisahy.board.global.error.ErrorCode

class PostNotFoundException: DomainException(ErrorCode.POST_NOT_FOUND)