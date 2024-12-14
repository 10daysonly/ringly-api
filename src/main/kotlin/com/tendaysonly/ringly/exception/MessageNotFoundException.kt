package com.tendaysonly.ringly.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * @author oognuyh
 */
class MessageNotFoundException :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found.")