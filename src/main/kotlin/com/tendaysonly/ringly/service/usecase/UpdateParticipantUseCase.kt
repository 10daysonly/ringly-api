package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Command
import com.tendaysonly.ringly.entity.Participant
import com.tendaysonly.ringly.service.User
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface UpdateParticipantUseCase {

    data class UpdateParticipantCommand(

        @field:NotNull
        val gatheringId: String,

        @field:NotNull
        val participantId: String,

        @field:NotNull
        val triggeredBy: User,

        var imageUrl: String?,

        var status: Participant.ParticipantStatus?
    ) : Command<Participant>

    fun updateParticipant(@Valid command: UpdateParticipantCommand): Participant
}