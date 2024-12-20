package com.tendaysonly.ringly.controller.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
data class CreateGatheringRequest(

    @Schema(description = "주최자 이메일", example = "host@10daysonly.com")
    val hostEmail: String? = null,

    @Schema(description = "주최자명", example = "강주최")
    val hostName: String? = null,

    @Schema(description = "모임명", example = "송년회")
    val name: String,

    @Schema(
        description = "Base64 | URL image",
        example = "https://dimg.donga.com/wps/NEWS/IMAGE/2023/05/12/119255016.1.jpg"
    )
    var image: String,

    @Schema(
        description = "장소",
        example = "서울시 서초구 서초대로 77길 17, 11층"
    )
    val location: String? = null,

    @Schema(
        description = "드레스 코드",
        example = "캐주얼"
    )
    val dressCode: String? = null,

    @Schema(
        description = "기타 추가 안내 사항",
        example = "주차 가능"
    )
    val additionalInfo: String? = null,

    @Schema(description = "소개 메세지")
    val intro: String? = null,

    @Schema(
        description = "모임일",
        example = "2024-12-31T19:00:00+09:00"
    )
    val meetAt: ZonedDateTime
)