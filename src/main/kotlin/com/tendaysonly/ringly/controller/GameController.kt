package com.tendaysonly.ringly.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.tendaysonly.ringly.config.WebSocketConfig
import com.tendaysonly.ringly.controller.dto.GameResponse
import com.tendaysonly.ringly.controller.dto.StartGameRequest
import com.tendaysonly.ringly.controller.specification.GameApiSpecification
import com.tendaysonly.ringly.cqrs.CommandBus
import com.tendaysonly.ringly.cqrs.QueryBus
import com.tendaysonly.ringly.security.Authenticated
import com.tendaysonly.ringly.service.User
import com.tendaysonly.ringly.service.usecase.GetActiveGameUseCase
import com.tendaysonly.ringly.service.usecase.StartGameUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.socket.TextMessage

@Controller
@RequestMapping("/api/v1/gatherings/{gatheringId}/games")
class GameController(
    private val commandBus: CommandBus,
    private val queryBus: QueryBus,
    private val participantWebSocketSessionRegistry: WebSocketConfig.ParticipantWebSocketSessionRegistry,
    private val mapper: ObjectMapper
) : GameApiSpecification {

    @Authenticated
    @GetMapping("/active")
    override fun getActiveGame(@PathVariable gatheringId: String): ResponseEntity<GameResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                GameResponse.from(
                    this.queryBus.execute(
                        GetActiveGameUseCase.GetActiveGameQuery(
                            gatheringId = gatheringId
                        )
                    )
                )
            )
    }

    @Authenticated
    @PostMapping
    override fun startGame(
        @AuthenticationPrincipal currentUser: User,
        @PathVariable gatheringId: String,
        @RequestBody request: StartGameRequest
    ): ResponseEntity<GameResponse> {

        val game = this.commandBus.execute(
            StartGameUseCase.StartGameCommand(
                gatheringId = gatheringId,
                type = request.type,
                triggeredBy = currentUser
            )
        )

        participantWebSocketSessionRegistry
            .getGathering(gatheringId)
            .entries
            .forEach { entry ->

                entry.value.sendMessage(
                    TextMessage(
                        mapper.writeValueAsString(
                            WebSocketConfig.Payload(
                                type = WebSocketConfig.Payload.PayloadType.GAME_RESULT,
                                data = mapper.convertValue(game, ObjectNode::class.java)
                            )
                        )
                    )
                )
            }

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                GameResponse.from(
                    game = game
                )
            )
    }
}