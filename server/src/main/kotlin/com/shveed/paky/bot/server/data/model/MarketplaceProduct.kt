package com.shveed.paky.bot.server.data.model

data class MarketplaceProduct(
  val marketplace: String,
  val productTitle: String,
  val reference: String,
  val rating: Double? = null,
)
