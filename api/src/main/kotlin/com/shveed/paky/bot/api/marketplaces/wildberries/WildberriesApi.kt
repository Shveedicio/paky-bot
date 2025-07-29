package com.shveed.paky.bot.api.marketplaces.wildberries

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "wildberries-api", url = "\${api.wildberries.url}")
interface WildberriesApi {
  @GetMapping("/search")
  fun searchProducts(@RequestParam("query") query: String): List<ProductResult>
}
