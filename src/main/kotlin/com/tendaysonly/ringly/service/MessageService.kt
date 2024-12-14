package com.tendaysonly.ringly.service

import com.tendaysonly.ringly.cqrs.CommandHandler
import com.tendaysonly.ringly.cqrs.QueryHandler
import com.tendaysonly.ringly.entity.Message
import com.tendaysonly.ringly.exception.GatheringNotFoundException
import com.tendaysonly.ringly.exception.MessageNotFoundException
import com.tendaysonly.ringly.repository.GatheringRepository
import com.tendaysonly.ringly.repository.MessageRepository
import com.tendaysonly.ringly.service.usecase.DeleteMessageUseCase
import com.tendaysonly.ringly.service.usecase.FindMessagesUseCase
import com.tendaysonly.ringly.service.usecase.PostMessageUseCase
import io.viascom.nanoid.NanoId
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val gatheringRepository: GatheringRepository
) : PostMessageUseCase, FindMessagesUseCase, DeleteMessageUseCase {

    @Transactional
    @CommandHandler
    override fun postMessage(command: PostMessageUseCase.PostMessageCommand): Message {

        val gathering = gatheringRepository.findByIdOrNull(command.gatheringId)
            ?: throw GatheringNotFoundException()

        return messageRepository.save(
            Message(
                messageId = NanoId.generate(),
                gathering = gathering,
                sender = command.sender,
                recipient = command.recipient,
                content = command.content,
                createdAt = ZonedDateTime.now()
            )
        )
    }

    @QueryHandler
    @Transactional(readOnly = true)
    override fun findMessages(query: FindMessagesUseCase.FindMessagesQuery): Page<Message> {

        val gathering = gatheringRepository.findByIdOrNull(query.gatheringId)
            ?: throw GatheringNotFoundException()

        return messageRepository.findByGathering(gathering = gathering, pageable = query.pageable)
    }

    @Transactional
    @CommandHandler
    override fun deleteMessage(command: DeleteMessageUseCase.DeleteMessageCommand): Message {

        val message =
            messageRepository.findByIdOrNull(command.messageId) ?: throw MessageNotFoundException()

        if (message.sender != command.triggeredBy.email) {

            throw GatheringNotFoundException()
        }

        messageRepository.delete(message)

        return message
    }
}