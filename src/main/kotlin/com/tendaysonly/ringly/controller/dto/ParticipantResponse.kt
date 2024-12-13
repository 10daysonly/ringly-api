package com.tendaysonly.ringly.controller.dto

import com.tendaysonly.ringly.entity.Participant
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
data class ParticipantResponse(

    @Schema(description = "참가자 고유 번호")
    val participantId: String,

    @Schema(description = "참가자 이름")
    val name: String,

    @Schema(description = "참가자 이메일")
    val email: String,

    @Schema(description = "참가자 이미지 주소")
    val imageUrl: String? = null,

    @Schema(description = "참가일시")
    val joinedAt: ZonedDateTime
) {

    companion object {

        fun from(participant: Participant): ParticipantResponse {

            return ParticipantResponse(
                participantId = participant.participantId,
                name = participant.name,
                email = participant.email,
                imageUrl = participant.imageUrl,
                joinedAt = participant.joinedAt
            )
        }
    }
}
