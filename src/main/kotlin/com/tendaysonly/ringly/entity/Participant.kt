package com.tendaysonly.ringly.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Table(name = "`participant`")
@Entity(name = "Participant")
class Participant(

    @Id
    val participantId: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var imageUrl: String? = null,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    var gathering: Gathering? = null,

    @Column(nullable = false)
    var joinedAt: ZonedDateTime,

    @Column(nullable = false)
    var isHost: Boolean = false
) {
}