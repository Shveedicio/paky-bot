package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.server.data.entity.ImageRequestTask
import com.shveed.paky.bot.server.data.repository.ImageRequestTaskRepository
import com.shveed.paky.bot.server.handler.TelegramBotMessageHandler
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class ResponseGenerationService(
  private val telegramBotMessageHandler: TelegramBotMessageHandler,
  private val imageRequestTaskRepository: ImageRequestTaskRepository,
) {

  fun generateAndSendResponse(imageRequestTask: ImageRequestTask) {
    try {
      imageRequestTask.status = ImageRequestTask.Status.GENERATING_RESPONSE
      imageRequestTaskRepository.saveAndFlush(imageRequestTask)

      val productAnalysis = imageRequestTask.payload
      if (productAnalysis.isNullOrBlank()) {
        throw RuntimeException("No product analysis available")
      }

      // Format the response message
      val responseMessage = formatProductResponse(productAnalysis)

      // Send the response to the user
      telegramBotMessageHandler.sendMessage(imageRequestTask.chatId, responseMessage)

      imageRequestTask.status = ImageRequestTask.Status.SUCCESS
      log.info { "Response sent successfully to chat ${imageRequestTask.chatId}" }
    } catch (ex: Exception) {
      log.error(ex) { "Error generating response for task ${imageRequestTask.id}" }
      imageRequestTask.status = ImageRequestTask.Status.ERROR

      // Send error message to user
      telegramBotMessageHandler.sendMessage(
        imageRequestTask.chatId,
        "❌ Произошла ошибка при поиске товаров. Попробуйте ещё раз.",
      )
    }

    imageRequestTaskRepository.saveAndFlush(imageRequestTask)
  }

  private fun formatProductResponse(productAnalysis: String): String = """
      🛍️ **Найденные товары:**

      $productAnalysis

      💡 *Результаты предоставлены на основе анализа изображения с помощью ИИ*
  """.trimIndent()
}
