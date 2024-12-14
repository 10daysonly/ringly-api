package com.tendaysonly.ringly.service

import com.tendaysonly.ringly.cqrs.CommandHandler
import com.tendaysonly.ringly.entity.Gathering
import com.tendaysonly.ringly.entity.Participant
import com.tendaysonly.ringly.exception.AlreadyParticipatingException
import com.tendaysonly.ringly.exception.GatheringNotFoundException
import com.tendaysonly.ringly.exception.NoPermissionException
import com.tendaysonly.ringly.exception.ParticipantNotFoundException
import com.tendaysonly.ringly.repository.GatheringRepository
import com.tendaysonly.ringly.repository.ParticipantRepository
import com.tendaysonly.ringly.service.usecase.JoinGatheringUseCase
import com.tendaysonly.ringly.service.usecase.UpdateParticipantUseCase
import io.viascom.nanoid.NanoId
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class ParticipantService(
    private val mailService: MailService,
    private val participantRepository: ParticipantRepository,
    private val gatheringRepository: GatheringRepository
) : JoinGatheringUseCase, UpdateParticipantUseCase {

    /**
     * Joins the gathering.
     */
    @Transactional
    @CommandHandler
    override fun joinGathering(command: JoinGatheringUseCase.JoinGahteringCommand): Gathering {

        val gathering =
            gatheringRepository.findByIdOrNull(command.gatheringId)
                ?: throw GatheringNotFoundException()

        this.participantRepository
            .findByEmail(command.email)
            ?.let { participant ->

                mailService.sendInvitation(
                    MailService.Recipient(
                        email = participant.email,
                        name = participant.name
                    ), gathering
                )

                throw AlreadyParticipatingException()
            }

        return gathering.apply {

            participants.add(
                Participant(
                    participantId = NanoId.generate(),
                    gathering = this,
                    joinedAt = ZonedDateTime.now(),
                    name = command.name,
                    email = command.email,
                    isHost = false
                )
            )

            gatheringRepository.save(this)

            mailService.sendInvitation(
                MailService.Recipient(
                    email = command.email,
                    name = command.name
                ), gathering
            )
        }
    }

    @Transactional
    @CommandHandler
    override fun updateParticipant(command: UpdateParticipantUseCase.UpdateParticipantCommand): Participant {

        val participant = participantRepository.findByIdOrNull(command.participantId)
            ?: throw ParticipantNotFoundException()

        if (participant.gathering.gatheringId != command.gatheringId) throw ParticipantNotFoundException()

        if (participant.email != command.triggeredBy.email) throw NoPermissionException()

        return participantRepository
            .save(participant
                .apply {
                    command.imageUrl?.let { this.imageUrl = it }
                    command.status?.let { this.status = it }
                })
    }
}