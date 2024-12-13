package com.tendaysonly.ringly.controller.dto

import com.tendaysonly.ringly.entity.Game
import io.swagger.v3.oas.annotations.media.Schema

data class StartGameRequest(

    @Schema(description = "게임 유형")
    val type: Game.GameType,
)