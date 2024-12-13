package com.tendaysonly.ringly.repository

import com.tendaysonly.ringly.entity.Gathering
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @author oognuyh
 */
@Repository
interface GatheringRepository : JpaRepository<Gathering, String> 