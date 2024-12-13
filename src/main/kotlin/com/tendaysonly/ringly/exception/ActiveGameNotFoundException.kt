package com.tendaysonly.ringly.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * @author oognuyh
 */
class ActiveGameNotFoundException :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Active game not found")