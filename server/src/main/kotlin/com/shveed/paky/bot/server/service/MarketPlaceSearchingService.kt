package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.api.marketplaces.ProductResult
import com.shveed.paky.bot.api.marketplaces.ozon.OzonApi
import com.shveed.paky.bot.api.marketplaces.wildberries.WildberriesApi
import com.shveed.paky.bot.api.marketplaces.yandexmarket.YandexMarketApi
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class MarketPlaceSearchingService(
  private val ozonApi: OzonApi,
  private val wildberriesApi: WildberriesApi,
  private val yandexMarketApi: YandexMarketApi,
) {

  fun searchProductsOnAllMarketplaces(query: String): List<ProductResult> {
    val allResults = mutableListOf<ProductResult>()

    try {
      // Search on Ozon
      val ozonResults = ozonApi.search(query)
      allResults.addAll(ozonResults)
      log.info { "Found ${ozonResults.size} products on Ozon" }
    } catch (ex: Exception) {
      log.error(ex) { "Error searching on Ozon: ${ex.message}" }
    }

    try {
      // Search on Wildberries
      val wildberriesResults = wildberriesApi.searchProducts(query)
      allResults.addAll(wildberriesResults)
      log.info { "Found ${wildberriesResults.size} products on Wildberries" }
    } catch (ex: Exception) {
      log.error(ex) { "Error searching on Wildberries: ${ex.message}" }
    }

    try {
      // Search on Yandex Market
      val yandexResults = yandexMarketApi.searchProducts(query)
      allResults.addAll(yandexResults)
      log.info { "Found ${yandexResults.size} products on Yandex Market" }
    } catch (ex: Exception) {
      log.error(ex) { "Error searching on Yandex Market: ${ex.message}" }
    }

    // Sort by rating and price, return top 5
    return allResults
      .sortedWith(compareByDescending<ProductResult> { it.rating }.thenBy { it.price })
      .take(5)
  }
}
