package com.tendaysonly.ringly.service

import com.tendaysonly.ringly.cqrs.CommandHandler
import com.tendaysonly.ringly.cqrs.QueryHandler
import com.tendaysonly.ringly.entity.Game
import com.tendaysonly.ringly.entity.GameResult
import com.tendaysonly.ringly.entity.Participant
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
                .findByGatheringAndStatus(
                    gathering = gathering,
                    Participant.ParticipantStatus.ATTENDING,
                    Pageable.unpaged()
                )

            this.results = when (command.type) {
                Game.GameType.RANDOM_PICK -> {

                    mutableListOf(
                        GameResult(
                            resultId = NanoId.generate(),
                            game = this,
                            picked = participants
                                .content[if (participants.size > 1) Random.nextInt(participants.size) else 0]
                                .name,
                            createdAt = ZonedDateTime.now()
                        )
                    )
                }

                Game.GameType.SECRET_SANTA -> {
                    if (participants.size < 2) {

                        mutableListOf(
                            GameResult(
                                resultId = NanoId.generate(),
                                game = this,
                                giver = participants.content.first().name,
                                receiver = participants.content.first().name,
                                createdAt = ZonedDateTime.now()
                            )
                        )
                    } else {

                        val givers = participants.toMutableList()
                        val receivers = givers.toMutableList()

                        do {
                            receivers.shuffle()
                        } while (receivers.indices.any { i -> givers[i].email == receivers[i].email })

                        givers
                            .zip(receivers)
                            .map { pair ->
                                GameResult(
                                    resultId = NanoId.generate(),
                                    game = this,
                                    giver = pair.first.name,
                                    receiver = pair.second.name,
                                    createdAt = ZonedDateTime.now()
                                )
                            }
                            .toMutableList()
                    }
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