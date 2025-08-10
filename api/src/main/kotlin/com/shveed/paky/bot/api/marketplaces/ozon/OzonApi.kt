package com.shveed.paky.bot.api.marketplaces.ozon

import com.shveed.paky.bot.api.marketplaces.ProductResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

interface OzonApi {
  @GetMapping("/products/search")
  fun search(@RequestParam query: String): List<ProductResult>
}
