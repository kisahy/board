package com.kisahy.board.domain.post.exception

class PostAlreadyDeletedException(message: String = "이미 삭제된 게시글입니다.") : RuntimeException(message)