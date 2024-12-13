package com.tendaysonly.ringly.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Table(name = "`message`")
@Entity(name = "Message")
class Message(

    @Id
    val messageId: String,

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "gathering_id")
    var gathering: Gathering,

    var content: String,

    var sender: String,

    var recipient: String,

    var createdAt: ZonedDateTime = ZonedDateTime.now(),
)