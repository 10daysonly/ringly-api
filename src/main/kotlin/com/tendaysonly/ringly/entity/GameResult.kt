package com.tendaysonly.ringly.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Entity(name = "GameResult")
@Table(name = "`game_result`")
class GameResult(

    @Id
    val resultId: String,

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "game_id")
    var game: Game,

    @Column(nullable = true)
    var picked: String? = null,

    @Column(nullable = true)
    var giver: String? = null,

    @Column(nullable = true)
    var receiver: String? = null,

    @Column(nullable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now()
)