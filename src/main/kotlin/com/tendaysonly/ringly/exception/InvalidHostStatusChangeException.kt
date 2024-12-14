package com.tendaysonly.ringly.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * @author oognuyh
 */
class InvalidHostStatusChangeException : ResponseStatusException(
    HttpStatus.CONFLICT,
    "Hosts must remain as active participants in the event."
)