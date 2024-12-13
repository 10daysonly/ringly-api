package com.tendaysonly.ringly.controller.dto

import com.tendaysonly.ringly.entity.Gathering
import com.tendaysonly.ringly.entity.Participant
import com.tendaysonly.ringly.exception.ParticipantNotFoundException
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

data class GatheringResponse(

    @Schema(
        description = "모임 고유 번호",
        example = "V1StGXR8_Z5jdHi6B-myT"
    )
    val gatheringId: String,

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
        description = "주최자",
    )
    val host: ParticipantResponse,

    @Schema(description = "모임명", example = "송년회")
    val name: String,

    @Schema(
        description = "장소",
        example = "서울시 서초구 서초대로 77길 17, 11층"
    )
    val location: String? = null,

    @Schema(
        description = "드레스 코드",
        example = "캐주얼"
    )
    var dressCode: String? = null,

    @Schema(
        description = "기타 추가 안내 사항",
        example = "주차 가능"
    )
    var additionalInfo: String? = null,

    @Schema(description = "소개 메세지")
    val intro: String? = null,

    var participants: MutableList<Participant> = mutableListOf(),

    @Schema(
        description = "생성일자",
        example = "2024-12-31T19:00:00+09:00"
    )
    var createdAt: ZonedDateTime,
) {

    companion object {

        fun from(gathering: Gathering): GatheringResponse {

            return GatheringResponse(
                gatheringId = gathering.gatheringId,
                imageUrl = gathering.imageUrl,
                meetAt = gathering.meetAt,
                additionalInfo = gathering.additionalInfo,
                participants = gathering.participants.toMutableList(),
                name = gathering.name,
                location = gathering.location,
                intro = gathering.intro,
                dressCode = gathering.dressCode,
                host = ParticipantResponse
                    .from(gathering.participants
                        .find { participant -> participant.email == gathering.host }
                        ?: throw ParticipantNotFoundException()),
                createdAt = gathering.createdAt,
            )
        }
    }
}
