package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Command
import com.tendaysonly.ringly.entity.Message
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface PostMessageUseCase {

    data class PostMessageCommand(

        @field:NotNull
        val gatheringId: String,

        @field:NotEmpty
        val content: String,

        @field:NotNull
        val recipient: String,

        @field:NotNull
        val sender: String
    ) : Command<Message>

    fun postMessage(@Valid command: PostMessageCommand): Message
}