package com.kisahy.board.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val accountId: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var password: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),
)