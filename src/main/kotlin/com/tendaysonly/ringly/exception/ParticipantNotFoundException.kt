package com.tendaysonly.ringly.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * @author oognuyh
 */
class ParticipantNotFoundException :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found.")