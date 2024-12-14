package com.tendaysonly.ringly.service

import com.tendaysonly.ringly.cqrs.CommandHandler
import com.tendaysonly.ringly.cqrs.QueryHandler
import com.tendaysonly.ringly.entity.Gathering
import com.tendaysonly.ringly.entity.Participant
import com.tendaysonly.ringly.exception.GatheringNotFoundException
import com.tendaysonly.ringly.exception.NoPermissionException
import com.tendaysonly.ringly.repository.GatheringRepository
import com.tendaysonly.ringly.repository.ParticipantRepository
import com.tendaysonly.ringly.service.usecase.*
import io.viascom.nanoid.NanoId
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class GatheringService(
    private val repository: GatheringRepository,
    private val participantRepository: ParticipantRepository,
    private val imageService: ImageService,
    private val mailService: MailService,
) : FindGatheringsUseCase, GetGatheringByGatheringIdUseCase, CreateGatheringUseCase,
    UpdateGatheringUseCase, DeleteGatheringUseCase, FindParticipantsInGatheringUseCase {

    /**
     * Checks if a target string is URL or not.
     */
    private fun isUrl(target: String) = target.matches("^https?://.*$".toRegex())

    /**
     * Creates a new gathering.
     */
    @Transactional
    @CommandHandler
    override fun createGathering(command: CreateGatheringUseCase.CreateGatheringCommand): Gathering {

        val gathering = Gathering(
            gatheringId = NanoId.generate(),
            name = command.name,
            imageUrl = if (this.isUrl(command.image)) {
                command.image
            } else {
                this.imageService.uploadImage(command.image)
            },
            host = command.hostEmail,
            intro = command.intro,
            meetAt = command.meetAt,
            location = command.location,
            dressCode = command.dressCode,
            additionalInfo = command.additionalInfo,
            createdAt = ZonedDateTime.now(),
        ).run {

            this.participants = mutableListOf(
                Participant(
                    participantId = NanoId.generate(),
                    name = command.hostName,
                    email = command.hostEmail,
                    joinedAt = ZonedDateTime.now(),
                    gathering = this,
                    isHost = true,
                )
            )

            repository.save(this)
        }

        mailService.sendInvitation(
            MailService.Recipient(email = command.hostEmail, name = command.hostName),
            gathering
        )

        return gathering
    }

    /**
     * Replaces the existing gathering with the updated one.
     */
    @Transactional
    @CommandHandler
    override fun updateGathering(command: UpdateGatheringUseCase.UpdateGatheringCommand): Gathering {

        val gathering =
            this.repository.findByIdOrNull(command.gatheringId)
                ?: throw GatheringNotFoundException()

        return this.repository.save(gathering.apply {
            name = command.name
            imageUrl = if (isUrl(command.image)) {
                command.image
            } else {
                imageService.uploadImage(command.image)
            }
            location = command.location
            intro = command.intro
            dressCode = command.dressCode
            additionalInfo = command.additionalInfo
            meetAt = command.meetAt
        })
    }

    /**
     * Deletes the existing gathering.
     */
    @Transactional
    @CommandHandler
    override fun deleteGathering(command: DeleteGatheringUseCase.DeleteGatheringCommand): Gathering {

        val gathering =
            this.repository.findByIdOrNull(command.gatheringId)
                ?: throw GatheringNotFoundException()

        if (!gathering.isHost(command.email)) {

            throw NoPermissionException()
        }

        this.repository.delete(gathering)

        return gathering
    }

    @QueryHandler
    @Transactional(readOnly = true)
    override fun findGatherings(query: FindGatheringsUseCase.FindGatheringsQuery): Page<Gathering> {

        return this.repository.findAll(query.pageable)
    }

    /**
     * Gets a query by ID.
     */
    @QueryHandler
    @Transactional(readOnly = true)
    override fun getGatheringByGatheringId(query: GetGatheringByGatheringIdUseCase.GetGatheringByGatheringIdQuery): Gathering {

        return this.repository.findByIdOrNull(query.gatheringId)
            ?: throw GatheringNotFoundException()
    }

    /**
     * Finds participants in gathering.
     *
     * @TODO(oognuyh) - Move the method
     */
    @QueryHandler
    @Transactional(readOnly = true)
    override fun findParticipantsInGathering(query: FindParticipantsInGatheringUseCase.FindParticipantsInGatheringQuery): Page<Participant> {

        try {
            val gathering =
                this.repository.getReferenceById(query.gatheringId)

            return this.participantRepository
                .findByGathering(gathering = gathering, pageable = query.pageable)
        } catch (e: EntityNotFoundException) {

            throw GatheringNotFoundException()
        }
    }
}