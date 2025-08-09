package com.shveed.paky.bot.server.service

import org.springframework.stereotype.Service

@Service
class ImageAnalysingService(
  private val visionApiService: VisionApiService,
  private val marketplaceService: MarketplaceService,
) {

}
