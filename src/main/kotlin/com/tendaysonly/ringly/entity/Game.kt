package com.tendaysonly.ringly.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Entity(name = "Game")
@Table(name = "`game`")
class Game(

    @Id
    val gameId: String,

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "gathering_id")
    var gathering: Gathering,

    @Enumerated(EnumType.STRING)
    var type: GameType,

    var isActive: Boolean = false,

    @JsonManagedReference
    @OneToMany(mappedBy = "game", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var results: MutableList<GameResult> = mutableListOf(),

    var createdBy: String,

    @Column(nullable = false)
    var createdAt: ZonedDateTime = ZonedDateTime.now(),
) {

    enum class GameType {

        @JsonProperty("random_pick")
        RANDOM_PICK,

        @JsonProperty("secret_santa")
        SECRET_SANTA
    }
}