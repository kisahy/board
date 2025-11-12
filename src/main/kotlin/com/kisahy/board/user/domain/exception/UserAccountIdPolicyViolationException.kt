package com.kisahy.board.user.domain.exception

import com.kisahy.board.global.error.DomainException
import com.kisahy.board.global.error.ErrorCode

class UserAccountIdPolicyViolationException: DomainException(ErrorCode.USER_ACCOUNT_ID_POLICY_VIOLATION)