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
  private val perplexityVisionService: PerplexityVisionService,
) {
  fun analyzeImage(imageRequestTask: ImageRequestTask) {
    imageRequestTask.apply { this.status = ImageRequestTask.Status.IMAGE_PROCESSING }
    imageRequestTaskRepository.saveAndFlush(imageRequestTask)

    try {
      val imageByteArray = telegramBotMessageHandler.downloadImageFile(imageRequestTask.imageId)
      val productAnalysis = perplexityVisionService.analyzeImageAndSearchProducts(imageByteArray)

      imageRequestTask.payload = productAnalysis
      imageRequestTask.status = ImageRequestTask.Status.MARKETPLACE_ANALYSIS
      log.info { "Product analysis completed: $productAnalysis" }
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
