package com.tendaysonly.ringly.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException

/**
 * @author oognuyh
 */
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(e.statusCode)
            .body(ErrorResponse(e.reason))

    @ExceptionHandler
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> = ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse(e.message))
}