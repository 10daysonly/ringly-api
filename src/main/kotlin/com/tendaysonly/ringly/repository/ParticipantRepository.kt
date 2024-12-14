package com.tendaysonly.ringly.repository

import com.tendaysonly.ringly.entity.Gathering
import com.tendaysonly.ringly.entity.Participant
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author oognuyh
 */
@Repository
interface ParticipantRepository : JpaRepository<Participant, String> {

    fun findByGatheringAndEmail(gathering: Gathering, email: String): Participant?

    fun findByGathering(gathering: Gathering, pageable: Pageable): Page<Participant>

    fun findByGatheringAndStatus(
        gathering: Gathering,
        status: Participant.ParticipantStatus,
        pageable: Pageable
    ): Page<Participant>
}