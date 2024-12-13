package com.tendaysonly.ringly.controller.specification

import com.tendaysonly.ringly.controller.dto.GameResponse
import com.tendaysonly.ringly.controller.dto.StartGameRequest
import com.tendaysonly.ringly.service.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

/*
 * @author oognuyh
 */
@Tag(name = "Game")
interface GameApiSpecification {

    @Operation(summary = "실행 중인 게임 조회")
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
    fun getActiveGame(
        @Parameter(
            description = "실행할 모임 고유 번호",
            required = true,
            example = "V1StGXR8_Z5jdHi6B-myT"
        ) gatheringId: String
    ): ResponseEntity<GameResponse>

    @Operation(summary = "게임 실행")
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
            responseCode = "500",
        )
    )
    fun startGame(
        @Parameter(hidden = true)
        currentUser: User,
        @Parameter(
            description = "실행할 모임 고유 번호",
            required = true,
            example = "V1StGXR8_Z5jdHi6B-myT"
        ) gatheringId: String,
        request: StartGameRequest
    ): ResponseEntity<GameResponse>
}