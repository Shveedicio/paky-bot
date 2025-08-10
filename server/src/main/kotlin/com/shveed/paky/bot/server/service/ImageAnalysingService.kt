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
) {
  fun analyzeImage(imageRequestTask: ImageRequestTask) {
    imageRequestTask.apply { this.status = ImageRequestTask.Status.IMAGE_PROCESSING }
    imageRequestTaskRepository.saveAndFlush(imageRequestTask)

    try {
      val imageByteArray =
        telegramBotMessageHandler.downloadImageFile(imageRequestTask.imageId)

      val description = openAIVisionService.analyzeImage(imageByteArray)
      log.info { "Image description: $description" }
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
      imageRequestTaskRepository.saveAndFlush(imageRequestTask)
    }
  }
}
