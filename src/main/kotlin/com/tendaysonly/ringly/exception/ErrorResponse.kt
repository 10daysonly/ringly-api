package com.tendaysonly.ringly.exception

import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
data class ErrorResponse(

    val message: String? = "An unexpected error occurred.",

    val timestamp: ZonedDateTime = ZonedDateTime.now(),
)
