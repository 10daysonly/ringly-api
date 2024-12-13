package com.tendaysonly.ringly.repository

import com.tendaysonly.ringly.entity.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author oognuyh
 */
interface GameRepository : JpaRepository<Game, String> {

    @Query("select game from Game game where game.gathering.gatheringId = :gatheringId and game.isActive = true")
    fun findActiveGame(@Param("gatheringId") gatheringId: String): Game?
}