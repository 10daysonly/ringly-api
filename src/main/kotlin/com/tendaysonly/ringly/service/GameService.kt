package com.tendaysonly.ringly.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.tendaysonly.ringly.config.WebSocketConfig
import com.tendaysonly.ringly.cqrs.CommandHandler
import com.tendaysonly.ringly.cqrs.QueryHandler
import com.tendaysonly.ringly.entity.Game
import com.tendaysonly.ringly.entity.GameResult
import com.tendaysonly.ringly.exception.ActiveGameNotFoundException
import com.tendaysonly.ringly.exception.GatheringNotFoundException
import com.tendaysonly.ringly.exception.NoPermissionException
import com.tendaysonly.ringly.repository.GameRepository
import com.tendaysonly.ringly.repository.GatheringRepository
import com.tendaysonly.ringly.repository.ParticipantRepository
import com.tendaysonly.ringly.service.usecase.GetActiveGameUseCase
import com.tendaysonly.ringly.service.usecase.StartGameUseCase
import io.viascom.nanoid.NanoId
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import kotlin.random.Random

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val gatheringRepository: GatheringRepository,
    private val participantRegistry: WebSocketConfig.ParticipantWebSocketSessionRegistry,
    private val mapper: ObjectMapper,
    private val participantRepository: ParticipantRepository
) : StartGameUseCase, GetActiveGameUseCase {

    @Transactional
    @CommandHandler
    override fun startGame(command: StartGameUseCase.StartGameCommand): Game {

        val gathering = gatheringRepository
            .findByIdOrNull(command.gatheringId) ?: throw GatheringNotFoundException()

        if (!gathering.isHost(command.triggeredBy.email)) {

            throw NoPermissionException()
        }

        gameRepository
            .findActiveGame(command.gatheringId)
            ?.let { activeGame ->

                activeGame.isActive = false

                gameRepository.save(activeGame)
            }

        val game = Game(
            gameId = NanoId.generate(),
            gathering = gathering,
            type = command.type,
            isActive = true,
            createdBy = command.triggeredBy.email,
            createdAt = ZonedDateTime.now()
        ).apply {

            val participants = participantRepository
                .findByGathering(gathering = gathering, Pageable.unpaged())

            this.results = when (command.type) {
                Game.GameType.RANDOM_PICK -> {

                    mutableListOf(
                        GameResult(
                            resultId = NanoId.generate(),
                            game = this,
                            picked = participants
                                .content[Random.nextInt(participants.size - 1)]
                                .email,
                            createdAt = ZonedDateTime.now()
                        )
                    )
                }

                Game.GameType.SECRET_SANTA -> {

                    val givers = participants.toMutableList()
                    val receivers = givers.toMutableList()

                    do {
                        receivers.shuffle()
                    } while (receivers.indices.any { i -> givers[i] == receivers[i] })

                    givers.zip(receivers).map { pair ->
                        GameResult(
                            resultId = NanoId.generate(),
                            game = this,
                            giver = pair.first.email,
                            receiver = pair.second.email,
                            createdAt = ZonedDateTime.now()
                        )
                    }.toMutableList()
                }
            }
        }

        return gameRepository.save(game)
    }

    @QueryHandler
    @Transactional(readOnly = true)
    override fun getActiveGame(query: GetActiveGameUseCase.GetActiveGameQuery): Game {

        val gathering = gatheringRepository.findByIdOrNull(query.gatheringId)
            ?: throw GatheringNotFoundException()

        return gameRepository.findActiveGame(gatheringId = gathering.gatheringId)
            ?: throw ActiveGameNotFoundException()
    }
}