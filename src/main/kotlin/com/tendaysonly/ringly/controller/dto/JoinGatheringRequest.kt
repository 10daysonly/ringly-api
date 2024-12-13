package com.tendaysonly.ringly.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

/**
 * @author oognuyh
 */
data class JoinGatheringRequest(

    @Schema(description = "참가자명")
    val name: String,

    @Schema(description = "이메일 주소", example = "oognuyh@ringly.com")
    val email: String
)