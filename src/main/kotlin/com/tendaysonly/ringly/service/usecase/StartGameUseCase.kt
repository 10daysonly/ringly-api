package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Command
import com.tendaysonly.ringly.entity.Game
import com.tendaysonly.ringly.service.User
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface StartGameUseCase {

    data class StartGameCommand(

        @field:NotNull
        val gatheringId: String,

        @field:NotNull
        val type: Game.GameType,

        @field:NotNull
        val triggeredBy: User
    ) : Command<Game>

    fun startGame(@Valid command: StartGameCommand): Game
}