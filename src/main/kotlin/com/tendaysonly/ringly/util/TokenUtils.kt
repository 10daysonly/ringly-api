package com.tendaysonly.ringly.util

import com.tendaysonly.ringly.security.InvalidTokenException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author oognuyh
 */
@Component
@EnableConfigurationProperties(TokenUtils.TokenProperties::class)
class TokenUtils(
    private val properties: TokenProperties,
) {

    @ConfigurationProperties(prefix = "token")
    data class TokenProperties(

        val secretKey: String
    )

    /**
     * Generates a new JWT using email and additional claims if exists.
     */
    fun generate(email: String, claims: Map<String, Any>? = mutableMapOf()): String = Jwts.builder()
        .setSubject(email)
        .addClaims(claims)
        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(properties.secretKey)))
        .compact()

    /**
     * Decodes the given token.
     */
    fun decode(token: String): Claims {

        try {

            return Jwts
                .parserBuilder()
                .setSigningKey(this.properties.secretKey)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {

            throw InvalidTokenException(message = e.message)
        }
    }
}