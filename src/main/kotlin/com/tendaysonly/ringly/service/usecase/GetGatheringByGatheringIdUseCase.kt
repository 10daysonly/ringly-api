package com.tendaysonly.ringly.service.usecase

import com.tendaysonly.ringly.cqrs.Query
import com.tendaysonly.ringly.entity.Gathering

/**
 * @author oognuyh
 */
interface GetGatheringByGatheringIdUseCase {

    data class GetGatheringByGatheringIdQuery(
        val gatheringId: String
    ) : Query<Gathering>

    fun getGatheringByGatheringId(query: GetGatheringByGatheringIdQuery): Gathering
}