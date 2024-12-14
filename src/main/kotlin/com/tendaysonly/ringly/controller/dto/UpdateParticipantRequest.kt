package com.tendaysonly.ringly.controller.dto

import com.tendaysonly.ringly.entity.Participant
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateParticipantRequest(

    @Schema(description = "이미지 주소")
    var imageUrl: String? = null,

    @Schema(description = "참가 상태")
    var status: Participant.ParticipantStatus? = null
)
