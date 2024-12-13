package com.tendaysonly.ringly.controller.specification

import com.tendaysonly.ringly.controller.dto.CreateGatheringRequest
import com.tendaysonly.ringly.controller.dto.GatheringPreviewResponse
import com.tendaysonly.ringly.controller.dto.GatheringResponse
import com.tendaysonly.ringly.controller.dto.UpdateGatheringRequest
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
@Tag(name = "Gathering")
interface GatheringApiSpecification {

    @Operation(summary = "현재 사용자의 모임 목록 조회")
    @SecurityRequirement(name = "JWT")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
        ),
        ApiResponse(
            responseCode = "401",
        ),
        ApiResponse(
            responseCode = "500",
        )
    )
    fun getGatherings(@ParameterObject pageable: Pageable): ResponseEntity<Page<GatheringResponse>>

    @Operation(summary = "특정 모임 조회")
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
    fun getGathering(
        @Parameter(description = "조회할 모임 고유 번호", required = true, example = "V1StGXR8_Z5jdHi6B-myT")
        gatheringId: String
    ): ResponseEntity<GatheringResponse>


    @Operation(summary = "특정 모임 미리 보기 조회")
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
    fun getGatheringPreview(
        @Parameter(description = "조회할 모임 고유 번호", required = true, example = "V1StGXR8_Z5jdHi6B-myT")
        gatheringId: String
    ): ResponseEntity<GatheringPreviewResponse>

    @Operation(summary = "모임 생성")
    @ApiResponses(
        ApiResponse(
            responseCode = "201",
        ),
        ApiResponse(
            responseCode = "422",
        ),
        ApiResponse(
            responseCode = "500",
        )
    )
    fun createGathering(
        currentUser: User?,
        request: CreateGatheringRequest
    ): ResponseEntity<GatheringResponse>

    @Operation(summary = "모임 변경")
    @SecurityRequirement(name = "JWT")
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
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
    fun updateGathering(
        @Parameter(description = "변경할 모임 고유 번호", required = true, example = "V1StGXR8_Z5jdHi6B-myT")
        gatheringId: String,
        request: UpdateGatheringRequest
    ): ResponseEntity<GatheringResponse>

    @Operation(summary = "모임 삭제")
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
    fun deleteGathering(
        @Parameter(description = "삭제할 모임 고유 번호", required = true, example = "V1StGXR8_Z5jdHi6B-myT")
        gatheringId: String
    ): ResponseEntity<GatheringResponse>
}