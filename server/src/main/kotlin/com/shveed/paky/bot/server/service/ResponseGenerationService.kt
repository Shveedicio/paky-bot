package com.shveed.paky.bot.server.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.shveed.paky.bot.server.data.entity.ImageRequestTask
import com.shveed.paky.bot.server.data.model.MarketplaceProduct
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
  private val objectMapper = ObjectMapper()

  fun generateAndSendResponse(imageRequestTask: ImageRequestTask) {
    try {
      imageRequestTask.status = ImageRequestTask.Status.GENERATING_RESPONSE
      imageRequestTaskRepository.saveAndFlush(imageRequestTask)

      val productAnalysisJson = imageRequestTask.payload
      if (productAnalysisJson.isNullOrBlank()) {
        throw RuntimeException("No product analysis available")
      }

      // Deserialize the JSON to get list of marketplace products
      val products = objectMapper.readValue(
        productAnalysisJson,
        object : TypeReference<List<MarketplaceProduct>>() {},
      )

      // Format the response message
      val responseMessage = formatProductResponse(products)

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

  private fun formatProductResponse(products: List<MarketplaceProduct>): String {
    if (products.isEmpty()) {
      return """
        🛍️ **Найденные товары на маркетплейсах:**

        ❌ Товары не найдены

        💡 *Попробуйте отправить другое изображение*
      """.trimIndent()
    }

    val productList = products.mapIndexed { index, product ->
      val ratingText = product.rating?.let { "⭐ ${String.format("%.1f", it)}" } ?: "⭐ Н/Д"
      val marketplaceEmoji = when (product.marketplace.lowercase()) {
        "ozon" -> "🟠"
        "wildberries" -> "🟣"
        "yandex market" -> "🟡"
        else -> "🏪"
      }

      """
        **${index + 1}.** $marketplaceEmoji **${product.marketplace}**
        📦 ${product.productTitle}
        $ratingText
        🔗 ${product.reference}
      """.trimIndent()
    }.joinToString("\n\n")

    return """
      🛍️ **Найденные товары на маркетплейсах:**

      $productList

      💡 *Результаты предоставлены на основе анализа изображения с помощью ИИ Perplexity*
      🔍 *Поиск выполнен на российских маркетплейсах: Ozon, Wildberries, Yandex Market*
    """.trimIndent()
  }
}
