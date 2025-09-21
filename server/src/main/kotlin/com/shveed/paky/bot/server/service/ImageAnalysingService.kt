package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.server.data.entity.ImageRequestTask
import com.shveed.paky.bot.server.data.repository.ImageRequestTaskRepository
import com.shveed.paky.bot.server.handler.TelegramBotMessageHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class ImageAnalysingService(
  private val telegramBotMessageHandler: TelegramBotMessageHandler,
  private val imageRequestTaskRepository: ImageRequestTaskRepository,
  private val openAIVisionService: OpenAIVisionService,
  private val perplexityVisionService: PerplexityVisionService,
) {
  fun analyzeImage(imageRequestTask: ImageRequestTask) {
    imageRequestTask.apply { this.status = ImageRequestTask.Status.IMAGE_PROCESSING }
    imageRequestTaskRepository.saveAndFlush(imageRequestTask)

    try {
      val imageByteArray = telegramBotMessageHandler.downloadImageFile(imageRequestTask.imageId)

      // Step 1: Use OpenAI Vision to analyze the image and get product description
      val productDescription = openAIVisionService.analyzeImage(imageByteArray)
      log.info { "OpenAI Vision description: $productDescription" }

      // Step 2: Use Perplexity to search for products based on the description
      val productSearchResults = perplexityVisionService.searchProductsBasedOnDescription(productDescription)
      log.info { "Perplexity search results: $productSearchResults" }

      imageRequestTask.payload = productSearchResults
      imageRequestTask.status = ImageRequestTask.Status.MARKETPLACE_ANALYSIS
      log.info { "Hybrid analysis completed successfully" }
    } catch (ex: Exception) {
      log.error(ex) { ex.message }

      imageRequestTask.apply {
        this.status = if (attemptCounter < 5) {
          ImageRequestTask.Status.CREATED
        } else {
          ImageRequestTask.Status.ERROR
        }
        this.attemptCounter++
      }
    }

    imageRequestTaskRepository.saveAndFlush(imageRequestTask)
  }
}
