package com.tendaysonly.ringly.controller

import com.tendaysonly.ringly.controller.dto.MessageResponse
import com.tendaysonly.ringly.controller.specification.MessageApiSpecification
import com.tendaysonly.ringly.cqrs.QueryBus
import com.tendaysonly.ringly.security.Authenticated
import com.tendaysonly.ringly.service.usecase.FindMessagesUseCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/v1/gatherings/{gatheringId}/messages")
class MessageController(
    private val queryBus: QueryBus
) : MessageApiSpecification {

    @GetMapping
    @Authenticated
    override fun findMessages(
        @PathVariable gatheringId: String,
        @PageableDefault(sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
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
}