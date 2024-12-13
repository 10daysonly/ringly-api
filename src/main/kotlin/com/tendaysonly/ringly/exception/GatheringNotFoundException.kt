package com.tendaysonly.ringly.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * @author oognuyh
 */
class GatheringNotFoundException :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Gathering not found.")