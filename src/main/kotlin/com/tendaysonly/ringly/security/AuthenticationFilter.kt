package com.tendaysonly.ringly.security

import com.tendaysonly.ringly.service.User
import com.tendaysonly.ringly.util.TokenUtils
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter(
    private val tokenUtils: TokenUtils
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        request
            .getHeader(HttpHeaders.AUTHORIZATION)
            ?.let { maybeToken ->

                val isValid = maybeToken.matches("^[bB]earer .*$".toRegex())
                if (!isValid) {

                    throw MissingBearerTokenException()
                }

                val claims =
                    this.tokenUtils.decode(maybeToken.replace("^[bB]earer ".toRegex(), ""))

                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(
                        User(email = claims.subject, name = claims["name"].toString()),
                        null,
                        // TODO(oognuyh) - Roles management needs to be improved
                        listOf(SimpleGrantedAuthority("ROLE_MEMBER"))
                    )
            }

        filterChain.doFilter(request, response)
    }
}