package com.kisahy.board.user.domain.exception

import com.kisahy.board.global.error.DomainException
import com.kisahy.board.global.error.ErrorCode

class UserAccountIdAlreadyExistsException: DomainException(ErrorCode.USER_ACCOUNT_ID_ALREADY_EXISTS)