package com.tendaysonly.ringly.controller.dto

import com.tendaysonly.ringly.entity.Gathering
import com.tendaysonly.ringly.exception.ParticipantNotFoundException
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
data class GatheringPreviewResponse(

    @Schema(
        description = "모임 고유 번호",
        example = "V1StGXR8_Z5jdHi6B-myT"
    )
    val gatheringId: String,

    @Schema(description = "모임명", example = "송년회")
    val name: String,

    @Schema(
        description = "Base64 | URL image",
        example = "https://dimg.donga.com/wps/NEWS/IMAGE/2023/05/12/119255016.1.jpg"
    )
    val imageUrl: String,

    @Schema(
        description = "모임일",
        example = "2024-12-31T19:00:00+09:00"
    )
    val meetAt: ZonedDateTime? = null,

    @Schema(
        description = "주최자 이메일",
        example = "oognuyh@ringly"
    )
    val host: ParticipantResponse,

    @Schema(
        description = "생성일자",
        example = "2024-12-31T19:00:00+09:00"
    )
    var createdAt: ZonedDateTime
) {

    companion object {

        fun from(gathering: Gathering): GatheringPreviewResponse {

            return GatheringPreviewResponse(
                gatheringId = gathering.gatheringId,
                name = gathering.name,
                imageUrl = gathering.imageUrl,
                host = ParticipantResponse
                    .from(
                        gathering
                            .participants
                            .find { participant -> participant.email == gathering.host }
                            ?: throw ParticipantNotFoundException()),
                meetAt = gathering.meetAt,
                createdAt = gathering.createdAt
            )
        }
    }
}