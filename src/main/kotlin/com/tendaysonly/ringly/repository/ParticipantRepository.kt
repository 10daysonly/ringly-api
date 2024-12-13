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

    fun findByEmail(email: String): Participant?

    fun findByGathering(gathering: Gathering, pageable: Pageable): Page<Participant>

    fun existsByGatheringAndEmail(gathering: Gathering, email: String): Boolean
}