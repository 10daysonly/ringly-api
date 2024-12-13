package com.tendaysonly.ringly.controller.dto

import com.tendaysonly.ringly.entity.Message
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
data class MessageResponse(

    @Schema(description = "메세지 고유 번호")
    var messageId: String,

    @Schema(description = "메세지 내용")
    var content: String,

    var recipient: String,

    var sender: String,

    var createdAt: ZonedDateTime
) {

    companion object {

        fun from(message: Message): MessageResponse {

            return MessageResponse(
                messageId = message.messageId,
                content = message.content,
                recipient = message.recipient,
                sender = message.sender,
                createdAt = message.createdAt
            )
        }
    }
}
