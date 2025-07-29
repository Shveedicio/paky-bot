package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.server.data.entity.ImageSearch
import com.shveed.paky.bot.server.data.repository.ImageSearchRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture

@Service
class ImageProcessingService(
// 	private val visionApiService: VisionApiService,
// 	private val marketplaceService: MarketplaceService,
  private val imageSearchRepository: ImageSearchRepository,
) {

  @Async
  fun processImage(userId: Long, imageBytes: ByteArray): CompletableFuture<ImageSearch> {
// 		val description = visionApiService.describeImage(imageBytes)
// 		val searchResults = marketplaceService.searchAllMarketplaces(description)

// 		val imageSearch = ImageSearch(
// 			userId = userId,
// 			description = description,
// 			searchResults = searchResults
// 		)
    TODO()
// 		return CompletableFuture.completedFuture(imageSearchRepository.save(imageSearch))
  }
}
