package com.kisahy.board.user.domain.exception

import com.kisahy.board.global.error.DomainException
import com.kisahy.board.global.error.ErrorCode

class UserPasswordPolicyViolationException: DomainException(ErrorCode.USER_PASSWORD_POLICY_VIOLATION)