package com.shveed.paky.bot.server.config.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "api")
data class ApiProps(
  var ozon: MarketplaceBlock,
  var wildberries: MarketplaceBlock,
  var yandexMarket: MarketplaceBlock,
) {
  data class MarketplaceBlock(var url: String, var key: String? = null)
}
