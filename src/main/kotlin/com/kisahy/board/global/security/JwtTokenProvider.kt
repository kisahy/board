package com.kisahy.board.global.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.access-secret}")
    private val accessSecret: String,

    @Value("\${jwt.refresh-secret}")
    private val refreshSecret: String,

    @Value("\${jwt.access-expiration}")
    private val accessExpiration: Long,

    @Value("\${jwt.refresh-expiration}")
    private val refreshExpiration: Long
) {
    private val accessKey = Keys.hmacShaKeyFor(accessSecret.toByteArray())
    private val refreshKey = Keys.hmacShaKeyFor(refreshSecret.toByteArray())

    private fun generateToken(userId: Long, secretKey: SecretKey, expiration: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String, secretKey: SecretKey): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun generateAccessToken(userId: Long): String = generateToken(userId, accessKey, accessExpiration)

    fun generateRefreshToken(userId: Long): String = generateToken(userId, refreshKey, refreshExpiration)

    fun validateAccessToken(token: String) = validateToken(token, accessKey)

    fun validateRefreshToken(token: String) = validateToken(token, refreshKey)

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(accessKey)
            .build()
            .parseClaimsJws(token)
            .body

        val username = claims.subject
        val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))

        val principal = User(username, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }
}