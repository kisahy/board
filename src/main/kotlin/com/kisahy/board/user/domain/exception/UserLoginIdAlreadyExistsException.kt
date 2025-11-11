package com.kisahy.board.user.domain.exception

import com.kisahy.board.global.error.DomainException
import com.kisahy.board.global.error.ErrorCode

class UserLoginIdAlreadyExistsException: DomainException(ErrorCode.USER_LOGIN_ID_ALREADY_EXISTS)