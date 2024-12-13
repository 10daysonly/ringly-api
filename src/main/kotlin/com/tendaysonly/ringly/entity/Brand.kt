package com.tendaysonly.ringly.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Table(name = "`brand`")
@Entity(name = "Brand")
class Brand(

    @Id
    val brandId: Number,

    val name: String,

    val imageUrl: String? = null,

    val createdAt: ZonedDateTime = ZonedDateTime.now(),
) {
}