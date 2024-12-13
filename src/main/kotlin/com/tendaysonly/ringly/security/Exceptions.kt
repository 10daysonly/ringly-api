package com.tendaysonly.ringly.security

import org.springframework.security.core.AuthenticationException

/**
 * @author oognuyh
 */
class MissingBearerTokenException :
    AuthenticationException("The Authentication header must contain a Bearer token.")

/**
 * @author oognuyh
 */
class InvalidTokenException(message: String?) :
    AuthenticationException(message ?: "The provided token is invalid or has expired.")