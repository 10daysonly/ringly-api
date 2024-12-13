package com.tendaysonly.ringly.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * @author oognuyh
 */
class AlreadyParticipatingException :
    ResponseStatusException(HttpStatus.CONFLICT, "Already participating.")