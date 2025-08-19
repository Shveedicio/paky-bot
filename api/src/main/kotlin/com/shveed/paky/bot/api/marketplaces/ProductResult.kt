package com.shveed.paky.bot.api.marketplaces

import java.math.BigDecimal

data class ProductResult(
  val id: String,
  val title: String,
  val url: String,
  val marketplace: String,
  val imageUrl: String? = null,
  val images: List<String> = emptyList(),
  val price: BigDecimal? = null,
  val originalPrice: BigDecimal? = null,
  val discount: Int? = null,
  val discountedPrice: BigDecimal? = null,
  val rating: Double? = null,
  val reviewsCount: Int? = null,
  val sellerName: String? = null,
  val sellerUrl: String? = null,
  val brand: String? = null,
  val category: String? = null,
  val description: String? = null,
  val availability: Boolean? = null,
  val vendorCode: String? = null,
  val commission: BigDecimal? = null,
  val deliveryOptions: List<String> = emptyList(),
  val specifications: Map<String, Any> = emptyMap(),
)
