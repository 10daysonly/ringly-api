package com.tendaysonly.ringly.entity

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.ZonedDateTime
import java.util.*

/**
 * @author oognuyh
 */
@Table(name = "`gathering`")
@Entity(name = "Gathering")
class Gathering(

    @Id
    val gatheringId: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var imageUrl: String,

    var location: String? = null,

    var dressCode: String? = null,

    var additionalInfo: String? = null,

    var intro: String? = null,

    var meetAt: ZonedDateTime? = null,

    var host: String,

    @JsonManagedReference
    @OneToMany(
        mappedBy = "gathering",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @BatchSize(size = 10)
    @OrderBy("isHost DESC, joinedAt ASC")
    var participants: MutableList<Participant> = mutableListOf(),

    var createdAt: ZonedDateTime = ZonedDateTime.now(),
) {

    /**
     * Checks if a target is equal to the email of the host.
     */
    fun isHost(maybe: String): Boolean {

        if (Objects.isNull(maybe)) {

            return false
        }

        return host == maybe
    }
}