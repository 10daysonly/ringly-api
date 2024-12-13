package com.tendaysonly.ringly.repository

import com.tendaysonly.ringly.entity.Gathering
import com.tendaysonly.ringly.entity.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author oognuyh
 */
@Repository
interface MessageRepository : JpaRepository<Message, String> {

    fun findByGathering(gathering: Gathering, pageable: Pageable): Page<Message>
}