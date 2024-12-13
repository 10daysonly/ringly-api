package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Command
import com.tendaysonly.ringly.entity.Gathering
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface DeleteGatheringUseCase {

    data class DeleteGatheringCommand(

        @field:NotNull
        val gatheringId: String,

        @field:NotNull
        val email: String
    ) : Command<Gathering>

    fun deleteGathering(@Valid command: DeleteGatheringCommand): Gathering
}