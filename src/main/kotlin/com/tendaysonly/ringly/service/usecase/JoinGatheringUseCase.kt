package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Command
import com.tendaysonly.ringly.entity.Gathering
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface JoinGatheringUseCase {

    data class JoinGahteringCommand(

        @field:NotNull
        val gatheringId: String,

        @field:NotEmpty
        val email: String,

        @field:NotEmpty
        val name: String
    ) : Command<Gathering>

    fun joinGathering(@Valid command: JoinGahteringCommand): Gathering
}