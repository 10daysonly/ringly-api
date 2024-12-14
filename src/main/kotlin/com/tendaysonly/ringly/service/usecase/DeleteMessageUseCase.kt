package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Command
import com.tendaysonly.ringly.entity.Message
import com.tendaysonly.ringly.service.User
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface DeleteMessageUseCase {

    data class DeleteMessageCommand(

        @field:NotNull
        val gatheringId: String,

        @field:NotNull
        val messageId: String,

        @field:NotNull
        val triggeredBy: User
    ) : Command<Message>

    fun deleteMessage(command: DeleteMessageCommand): Message
}