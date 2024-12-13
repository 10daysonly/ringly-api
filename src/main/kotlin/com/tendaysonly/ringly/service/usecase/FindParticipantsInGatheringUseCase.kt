package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Query
import com.tendaysonly.ringly.entity.Participant
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface FindParticipantsInGatheringUseCase {

    data class FindParticipantsInGatheringQuery(

        @field:NotNull
        val gatheringId: String,

        val pageable: Pageable
    ) : Query<Page<Participant>>

    fun findParticipantsInGathering(@Valid query: FindParticipantsInGatheringQuery): Page<Participant>
}