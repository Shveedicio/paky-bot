package com.shveed.paky.bot.api.marketplaces.wildberries

import com.shveed.paky.bot.api.marketplaces.ProductResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

interface WildberriesApi {
  @GetMapping("/search")
  fun searchProducts(@RequestParam("query") query: String): List<ProductResult>
}
