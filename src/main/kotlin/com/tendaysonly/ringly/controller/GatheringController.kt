package com.tendaysonly.ringly.controller

import com.tendaysonly.ringly.controller.dto.CreateGatheringRequest
import com.tendaysonly.ringly.controller.dto.GatheringPreviewResponse
import com.tendaysonly.ringly.controller.dto.GatheringResponse
import com.tendaysonly.ringly.controller.dto.UpdateGatheringRequest
import com.tendaysonly.ringly.controller.specification.GatheringApiSpecification
import com.tendaysonly.ringly.cqrs.CommandBus
import com.tendaysonly.ringly.cqrs.QueryBus
import com.tendaysonly.ringly.service.User
import com.tendaysonly.ringly.service.usecase.CreateGatheringUseCase
import com.tendaysonly.ringly.service.usecase.DeleteGatheringUseCase
import com.tendaysonly.ringly.service.usecase.GetGatheringByGatheringIdUseCase
import com.tendaysonly.ringly.service.usecase.UpdateGatheringUseCase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/v1/gatherings")
class GatheringController(
    private val commandBus: CommandBus,
    private val queryBus: QueryBus
) : GatheringApiSpecification {

    @GetMapping
    override fun getGatherings(@PageableDefault(page = 1) pageable: Pageable): ResponseEntity<Page<GatheringResponse>> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                null
            )
    }

    @GetMapping("/{gatheringId}")
    override fun getGathering(@PathVariable gatheringId: String): ResponseEntity<GatheringResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                GatheringResponse.from(
                    this.queryBus.execute(
                        GetGatheringByGatheringIdUseCase.GetGatheringByGatheringIdQuery(
                            gatheringId = gatheringId
                        )
                    )
                )
            )
    }

    @GetMapping("/{gatheringId}/preview")
    override fun getGatheringPreview(@PathVariable gatheringId: String): ResponseEntity<GatheringPreviewResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                GatheringPreviewResponse.from(
                    this.queryBus.execute(
                        GetGatheringByGatheringIdUseCase.GetGatheringByGatheringIdQuery(
                            gatheringId = gatheringId
                        )
                    )
                )
            )
    }

    @PostMapping
    override fun createGathering(
        @AuthenticationPrincipal
        currentUser: User?,
        @RequestBody request: CreateGatheringRequest
    ): ResponseEntity<GatheringResponse> {

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                GatheringResponse.from(
                    this.commandBus.execute(
                        CreateGatheringUseCase.CreateGatheringCommand(
                            name = request.name,
                            image = request.image,
                            location = request.location,
                            meetAt = request.meetAt,
                            dressCode = request.dressCode,
                            additionalInfo = request.additionalInfo,
                            intro = request.intro,
                            hostEmail = currentUser?.email ?: request.hostEmail,
                            hostName = currentUser?.name ?: request.hostName,
                        )
                    )
                )
            )
    }

    @PutMapping("/{gatheringId}")
    override fun updateGathering(
        @PathVariable gatheringId: String,
        @RequestBody request: UpdateGatheringRequest
    ): ResponseEntity<GatheringResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                GatheringResponse.from(
                    this.commandBus.execute(
                        UpdateGatheringUseCase.UpdateGatheringCommand(
                            gatheringId = gatheringId,
                            name = request.name,
                            image = request.image,
                            location = request.location,
                            meetAt = request.meetAt,
                            dressCode = request.dressCode,
                            additionalInfo = request.additionalInfo,
                            intro = request.intro,
                        )
                    )
                )
            )
    }

    @DeleteMapping("/{gatheringId}")
    override fun deleteGathering(@PathVariable gatheringId: String): ResponseEntity<GatheringResponse> {

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                GatheringResponse.from(
                    this.commandBus.execute(
                        DeleteGatheringUseCase.DeleteGatheringCommand(
                            gatheringId = gatheringId,
                            email = gatheringId
                        )
                    )
                )
            )
    }
}