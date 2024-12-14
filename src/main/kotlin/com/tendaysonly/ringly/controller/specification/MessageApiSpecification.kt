package com.tendaysonly.ringly.controller.specification

import com.tendaysonly.ringly.controller.dto.MessageResponse
import com.tendaysonly.ringly.service.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

/**
 * @author oognyuh
 */
@Tag(name = "Message")
interface MessageApiSpecification {

    @Operation(summary = "모임의 메세지 목록 조회")
    @SecurityRequirement(name = "JWT")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
        ),
        ApiResponse(
            responseCode = "401",
        ),
        ApiResponse(
            responseCode = "404",
        ),
        ApiResponse(
            responseCode = "500",
        )
    )
    fun findMessages(
        @Parameter(
            description = "조회할 모임 고유 번호",
            required = true,
            example = "V1StGXR8_Z5jdHi6B-myT"
        ) gatheringId: String,
        @ParameterObject
        pageable: Pageable
    ): ResponseEntity<Page<MessageResponse>>

    @Operation(summary = "메세지 삭제")
    @SecurityRequirement(name = "JWT")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
        ),
        ApiResponse(
            responseCode = "401",
        ),
        ApiResponse(
            responseCode = "403",
        ),
        ApiResponse(
            responseCode = "404",
        ),
        ApiResponse(
            responseCode = "500",
        )
    )
    fun deleteMessage(
        @Parameter(hidden = true)
        currentUser: User,
        @Parameter(
            description = "모임 고유 번호",
            required = true,
            example = "V1StGXR8_Z5jdHi6B-myT"
        ) gatheringId: String,
        @Parameter(
            description = "삭제할 메세지 고유 번호",
            required = true,
            example = "V1StGXR8_Z5jdHi6B-myT"
        ) messageId: String,
    ): ResponseEntity<MessageResponse>
}