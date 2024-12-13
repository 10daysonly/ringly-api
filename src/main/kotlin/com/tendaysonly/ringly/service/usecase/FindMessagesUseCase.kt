package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Query
import com.tendaysonly.ringly.entity.Message
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated

/**
 * @author oognuyh
 */
@Validated
interface FindMessagesUseCase {

    data class FindMessagesQuery(

        @field:NotNull
        val gatheringId: String,

        @field:NotNull
        val pageable: Pageable
    ) : Query<Page<Message>>

    fun findMessages(@Valid query: FindMessagesQuery): Page<Message>
}