package com.tendaysonly.ringly.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.tendaysonly.ringly.config.WebSocketConfig
import com.tendaysonly.ringly.controller.dto.MessageResponse
import com.tendaysonly.ringly.controller.specification.MessageApiSpecification
import com.tendaysonly.ringly.cqrs.CommandBus
import com.tendaysonly.ringly.cqrs.QueryBus
import com.tendaysonly.ringly.security.Authenticated
import com.tendaysonly.ringly.service.User
import com.tendaysonly.ringly.service.usecase.DeleteMessageUseCase
import com.tendaysonly.ringly.service.usecase.FindMessagesUseCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.socket.TextMessage

@Controller
@RequestMapping("/api/v1/gatherings/{gatheringId}/messages")
class MessageController(
    private val commandBus: CommandBus,
    private val queryBus: QueryBus,
    private val participantWebSocketSessionRegistry: WebSocketConfig.ParticipantWebSocketSessionRegistry,
    private val mapper: ObjectMapper
) : MessageApiSpecification {

    @GetMapping
    @Authenticated
    override fun findMessages(
        @PathVariable gatheringId: String,
        @PageableDefault(
            page = 1,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<Page<MessageResponse>> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                queryBus.execute(
                    FindMessagesUseCase.FindMessagesQuery(
                        gatheringId = gatheringId,
                        pageable = pageable
                    )
                ).map { message -> MessageResponse.from(message) }
            )
    }

    @Authenticated
    @DeleteMapping("/{messageId}")
    override fun deleteMessage(
        @AuthenticationPrincipal currentUser: User,
        @PathVariable gatheringId: String,
        @PathVariable messageId: String
    ): ResponseEntity<MessageResponse> {

        val message = commandBus.execute(
            DeleteMessageUseCase.DeleteMessageCommand(
                gatheringId = gatheringId,
                messageId = messageId,
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
                                type = WebSocketConfig.Payload.PayloadType.MESSAGE_DELETED,
                                data = mapper.convertValue(
                                    MessageResponse.from(message),
                                    ObjectNode::class.java
                                ),
                            )
                        )
                    )
                )
            }

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                MessageResponse.from(
                    message = message
                )
            )
    }
}