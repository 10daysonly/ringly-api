package com.tendaysonly.ringly.controller.dto

import com.tendaysonly.ringly.entity.Game
import com.tendaysonly.ringly.entity.GameResult
import io.swagger.v3.oas.annotations.media.Schema
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
data class GameResponse(

    @Schema(description = "게임 고유 번호")
    val gameId: String,

    @Schema(description = "모임 고유 번호")
    val gatheringId: String,

    @Schema(description = "활성화 여부")
    val isActive: Boolean,

    @Schema(description = "게임 결과")
    val results: List<GameResultResponse>,
) {

    data class GameResultResponse(

        @Schema(description = "선택된 참가자 이메일(제비 뽑기)")
        var picked: String? = null,

        @Schema(description = "받는 참가자 이메일(마니또)")
        var receiver: String? = null,

        @Schema(description = "주는 참가자 이메일(마니또)")
        var giver: String? = null,

        var createdAt: ZonedDateTime,
    ) {
        companion object {

            fun from(result: GameResult): GameResultResponse {

                return GameResultResponse(
                    picked = result.picked,
                    receiver = result.receiver,
                    giver = result.giver,
                    createdAt = result.createdAt,
                )
            }
        }
    }

    companion object {

        fun from(game: Game): GameResponse {

            return GameResponse(
                gameId = game.gameId,
                gatheringId = game.gathering.gatheringId,
                isActive = game.isActive,
                results = game.results.map { result -> GameResultResponse.from(result) },
            )
        }
    }
}
