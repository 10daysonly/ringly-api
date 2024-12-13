package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Command
import com.tendaysonly.ringly.entity.Gathering
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Validated
interface UpdateGatheringUseCase {

    data class UpdateGatheringCommand(

        @field:NotNull
        val gatheringId: String,

        @field:NotEmpty
        val name: String,

        @field:NotNull
        var image: String,

        val location: String? = null,

        var dressCode: String? = null,

        var additionalInfo: String? = null,

        val intro: String? = null,

        val meetAt: ZonedDateTime? = null
    ) : Command<Gathering>

    fun updateGathering(@Valid command: UpdateGatheringCommand): Gathering
}