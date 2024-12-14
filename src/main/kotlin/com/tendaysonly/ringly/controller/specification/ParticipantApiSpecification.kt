package com.tendaysonly.ringly.controller.specification

import com.tendaysonly.ringly.controller.dto.GatheringResponse
import com.tendaysonly.ringly.controller.dto.JoinGatheringRequest
import com.tendaysonly.ringly.controller.dto.ParticipantResponse
import com.tendaysonly.ringly.controller.dto.UpdateParticipantRequest
import com.tendaysonly.ringly.entity.Participant
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
 * @author oognuyh
 */
@Tag(name = "Participant")
interface ParticipantApiSpecification {

    @Operation(summary = "특정 모임의 참가자 목록 조회")
    @SecurityRequirement(name = "JWT")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
        ),
        ApiResponse(
            responseCode = "404",
        ),
        ApiResponse(
            responseCode = "500",
        )
    )
    fun findParticipants(
        @Parameter(
            description = "조회할 모임 고유 번호",
            required = true,
            example = "V1StGXR8_Z5jdHi6B-myT"
        ) gatheringId: String,
        @ParameterObject pageable: Pageable
    ): ResponseEntity<Page<Participant>>

    @Operation(summary = "모임 참여자 인증")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
        ),
        ApiResponse(
            responseCode = "404",
        ),
        ApiResponse(
            responseCode = "409",
        ),
        ApiResponse(
            responseCode = "422",
        ),
        ApiResponse(
            responseCode = "500",
        )
    )
    fun joinGathering(
        @Parameter(description = "인증할 모임 고유 번호", required = true, example = "V1StGXR8_Z5jdHi6B-myT")
        gatheringId: String,
        request: JoinGatheringRequest
    ): ResponseEntity<GatheringResponse>

    @Operation(summary = "참가자 정보 수정")
    @SecurityRequirement(name = "JWT")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
        ),
        ApiResponse(
            responseCode = "403",
        ),
        ApiResponse(
            responseCode = "404",
        ),
        ApiResponse(
            responseCode = "422",
        ),
        ApiResponse(
            responseCode = "500",
        )
    )
    fun updateParticipant(
        @Parameter(hidden = true)
        currentUser: User,
        @Parameter(description = "모임 고유 번호", required = true, example = "V1StGXR8_Z5jdHi6B-myT")
        gatheringId: String,
        @Parameter(description = "참가자 고유 번호", required = true, example = "V1StGXR8_Z5jdHi6B-myT")
        participantId: String,
        request: UpdateParticipantRequest
    ): ResponseEntity<ParticipantResponse>
}