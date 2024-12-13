package com.tendaysonly.ringly.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * @author oognuyh
 */
@Table(name = "`subcategory`")
@Entity(name = "Subcategory")
class Subcategory(

    @Id
    val subcategoryId: String,

    @Column(nullable = false, unique = true)
    var name: String
) {
}