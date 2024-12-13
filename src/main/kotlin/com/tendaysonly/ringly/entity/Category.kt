package com.tendaysonly.ringly.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Table(name = "`category`")
@Entity(name = "Category")
class Category(

    @Id
    val categoryId: String,

    val name: String,

    val createdAt: ZonedDateTime = ZonedDateTime.now(),
) {
}