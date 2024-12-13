package com.tendaysonly.ringly.entity

import jakarta.persistence.*
import java.time.ZonedDateTime

/**
 * @author oognuyh
 */
@Table(name = "`product`")
@Entity(name = "Product")
class Product(

    @Id
    val productId: String,

    @ManyToOne
    @JoinColumn(name = "brand_id")
    var brand: Brand,

    @Column(nullable = false, unique = true)
    var name: String,

    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category,

    @ManyToOne
    @JoinColumn(name = "subcatagory_id")
    var subcategory: Subcategory,

    var price: Int,

    var createdAt: ZonedDateTime = ZonedDateTime.now(),
) {
}