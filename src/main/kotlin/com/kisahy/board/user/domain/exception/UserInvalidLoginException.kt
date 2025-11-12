package com.kisahy.board.user.domain.exception

import com.kisahy.board.global.error.DomainException
import com.kisahy.board.global.error.ErrorCode

class UserInvalidLoginException: DomainException(ErrorCode.USER_INVALID_LOGIN)