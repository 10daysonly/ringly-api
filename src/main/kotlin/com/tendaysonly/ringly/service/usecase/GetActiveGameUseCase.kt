package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Query
import com.tendaysonly.ringly.entity.Game
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface GetActiveGameUseCase {

    data class GetActiveGameQuery(

        @field:NotNull
        val gatheringId: String
    ) : Query<Game>

    fun getActiveGame(@Valid query: GetActiveGameQuery): Game
}