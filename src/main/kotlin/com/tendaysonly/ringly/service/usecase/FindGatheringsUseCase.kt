package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Query
import com.tendaysonly.ringly.entity.Gathering
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface FindGatheringsUseCase {

    data class FindGatheringsQuery(

        val host: String,

        val pageable: Pageable
    ) : Query<Page<Gathering>>

    fun findGatherings(@Valid query: FindGatheringsQuery): Page<Gathering>
}