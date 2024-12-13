package com.tendaysonly.ringly.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * @author oognuyh
 */
class NoPermissionException : ResponseStatusException(
    HttpStatus.FORBIDDEN,
    "You have no permission to perform the operation."
)