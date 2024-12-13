package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Command
import com.tendaysonly.ringly.entity.Gathering
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import org.springframework.validation.annotation.Validated
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Validated
interface CreateGatheringUseCase {

    data class CreateGatheringCommand(

        val hostName: String,

        val hostEmail: String,

        @field:NotEmpty
        val name: String,

        @field:NotEmpty
        val image: String,

        val dressCode: String? = null,

        val location: String? = null,

        val intro: String? = null,

        val additionalInfo: String? = null,

        val meetAt: ZonedDateTime? = null
    ) : Command<Gathering>

    fun createGathering(@Valid command: CreateGatheringCommand): Gathering
}