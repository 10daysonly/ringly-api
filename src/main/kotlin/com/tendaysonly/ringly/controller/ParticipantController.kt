package com.tendaysonly.ringly.controller

import com.tendaysonly.ringly.controller.dto.GatheringResponse
import com.tendaysonly.ringly.controller.dto.JoinGatheringRequest
import com.tendaysonly.ringly.controller.dto.ParticipantResponse
import com.tendaysonly.ringly.controller.dto.UpdateParticipantRequest
import com.tendaysonly.ringly.controller.specification.ParticipantApiSpecification
import com.tendaysonly.ringly.cqrs.CommandBus
import com.tendaysonly.ringly.cqrs.QueryBus
import com.tendaysonly.ringly.entity.Participant
import com.tendaysonly.ringly.security.Authenticated
import com.tendaysonly.ringly.service.User
import com.tendaysonly.ringly.service.usecase.FindParticipantsInGatheringUseCase
import com.tendaysonly.ringly.service.usecase.JoinGatheringUseCase
import com.tendaysonly.ringly.service.usecase.UpdateParticipantUseCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/v1/gatherings/{gatheringId}/participants")
class ParticipantController(
    private val commandBus: CommandBus,
    private val queryBus: QueryBus
) : ParticipantApiSpecification {

    @GetMapping
    override fun findParticipants(
        @PathVariable gatheringId: String,
        @PageableDefault(page = 1) pageable: Pageable
    ): ResponseEntity<Page<Participant>> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                this.queryBus.execute(
                    FindParticipantsInGatheringUseCase.FindParticipantsInGatheringQuery(
                        gatheringId = gatheringId,
                        pageable = pageable
                    )
                )
            )
    }

    @PostMapping
    override fun joinGathering(
        @PathVariable gatheringId: String,
        @RequestBody request: JoinGatheringRequest
    ): ResponseEntity<GatheringResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                GatheringResponse.from(
                    this.commandBus.execute(
                        JoinGatheringUseCase.JoinGahteringCommand(
                            gatheringId = gatheringId,
                            email = request.email,
                            name = request.name,
                        )
                    )
                )
            )
    }

    @Authenticated
    @PatchMapping("/{participantId}")
    override fun updateParticipant(
        @AuthenticationPrincipal currentUser: User,
        @PathVariable gatheringId: String,
        @PathVariable participantId: String,
        @RequestBody request: UpdateParticipantRequest
    ): ResponseEntity<ParticipantResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                ParticipantResponse.from(
                    this.commandBus.execute(
                        UpdateParticipantUseCase.UpdateParticipantCommand(
                            gatheringId = gatheringId,
                            participantId = participantId,
                            imageUrl = request.imageUrl,
                            status = request.status,
                            triggeredBy = currentUser
                        )
                    )
                )
            )
    }
}