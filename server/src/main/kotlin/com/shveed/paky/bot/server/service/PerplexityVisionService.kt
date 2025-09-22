package com.shveed.paky.bot.server.service

import PerplexityResponse
import com.fasterxml.jackson.databind.ObjectMapper
import com.shveed.paky.bot.api.perplexity.PerplexityApi
import com.shveed.paky.bot.api.perplexity.model.PerplexityContentItem
import com.shveed.paky.bot.api.perplexity.model.PerplexityImageUrl
import com.shveed.paky.bot.api.perplexity.model.PerplexityMessage
import com.shveed.paky.bot.api.perplexity.model.PerplexityRequest
import com.shveed.paky.bot.server.config.props.AIProps
import com.shveed.paky.bot.server.constant.AiRequests.PERPLEXITY_PRODUCT_SEARCH_REQUEST_TEXT
import com.shveed.paky.bot.server.data.model.MarketplaceProduct
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.util.Base64

private val log = KotlinLogging.logger {}

@Service
class PerplexityVisionService(private val perplexityApi: PerplexityApi, private val aiProps: AIProps) {

  fun searchProductsBasedOnImage(imageBytes: ByteArray): List<MarketplaceProduct> {
    // Validate image size
    if (imageBytes.size > 20 * 1024 * 1024) { // 20MB limit
      throw IllegalArgumentException("Image too large. Maximum size is 20MB")
    }

    val base64Image = Base64.getEncoder().encode(imageBytes)

    // Detect image format
    val imageFormat = detectImageFormat(imageBytes)
    log.info { "Detected image format: $imageFormat" }

    val objectMapper = ObjectMapper()

    val content = listOf(
      PerplexityContentItem(
        type = "text",
        text = PERPLEXITY_PRODUCT_SEARCH_REQUEST_TEXT.trimIndent(),
      ),
      PerplexityContentItem(
        type = "image_url",
        imageUrl = PerplexityImageUrl(url = "data:$imageFormat;base64".toByteArray().plus(base64Image)),
      ),
    )

    val requestBody = PerplexityRequest(
      model = aiProps.perplexity.model,
      messages = listOf(
        PerplexityMessage(
          role = "user",
          content = objectMapper.writeValueAsString(content),
        ),
      ),
      maxTokens = 1500,
      temperature = 0.7,
    )

    log.info { "Searching products with Perplexity API for image size: ${imageBytes.size} bytes" }

    // Debug: Print the actual JSON that will be sent
    val jsonString = objectMapper.writeValueAsString(requestBody)
    log.info { "Perplexity request JSON: $jsonString" }

    log.info { "Making HTTP request to Perplexity API..." }

    val response = perplexityApi.analyzeImageAndSearchProducts(
      authorization = "Bearer ${aiProps.perplexity.key}",
      requestBody = requestBody,
    )

    if (!response.statusCode.is2xxSuccessful) {
      log.error { "Perplexity API error: ${response.statusCode.value()}, body: ${response.body}" }
      log.error { "Request that failed: $requestBody" }
      throw RuntimeException("Perplexity API error: ${response.statusCode.value()} - ${response.body}")
    }

    log.info { "Perplexity API request successful, parsing response..." }
    return parsePerplexityResponse(response.body)
  }

  private fun parsePerplexityResponse(perplexityResponse: PerplexityResponse): List<MarketplaceProduct> {
    try {
      val content = perplexityResponse.choices.firstOrNull()?.message?.content
        ?: throw RuntimeException("No content found in Perplexity response")
      log.info { "Perplexity response content: $content" }

      return parseProductsFromContent(content)
    } catch (ex: Exception) {
      log.error(ex) { "Failed to parse Perplexity response: $perplexityResponse" }
      throw RuntimeException("Failed to parse Perplexity response", ex)
    }
  }

  private fun parseProductsFromContent(content: String): List<MarketplaceProduct> {
    val products = mutableListOf<MarketplaceProduct>()

    // Split content by numbered items (1., 2., 3., etc.)
    val lines = content.split("\n").filter { it.trim().isNotEmpty() }

    var currentProduct: MarketplaceProduct? = null
    var currentMarketplace = ""
    var currentTitle = ""
    var currentReference = ""
    var currentRating: Double? = null

    for (line in lines) {
      val trimmedLine = line.trim()

      // Check if this is a new product (starts with number)
      if (trimmedLine.matches(Regex("^\\d+\\..*"))) {
        // Save previous product if exists
        if (currentProduct != null) {
          products.add(currentProduct)
        }

        // Start new product
        currentTitle = trimmedLine.substringAfter(".").trim()
        currentMarketplace = ""
        currentReference = ""
        currentRating = null
      }

      // Extract marketplace
      when {
        trimmedLine.contains("Ozon", ignoreCase = true) -> currentMarketplace = "Ozon"
        trimmedLine.contains("Wildberries", ignoreCase = true) -> currentMarketplace = "Wildberries"
        trimmedLine.contains("Yandex Market", ignoreCase = true) -> currentMarketplace = "Yandex Market"
      }

      // Extract rating (look for patterns like "4.5/5", "4.5⭐", etc.)
      val ratingMatch = Regex("(\\d+\\.?\\d*)\\s*[⭐/]?\\s*5?").find(trimmedLine)
      if (ratingMatch != null) {
        try {
          currentRating = ratingMatch.groupValues[1].toDouble()
        } catch (e: NumberFormatException) {
          // Ignore invalid rating
        }
      }

      // Extract reference/URL
      val urlMatch = Regex("https?://[^\\s]+").find(trimmedLine)
      if (urlMatch != null) {
        currentReference = urlMatch.value
      }
    }

    // Add the last product
    if (currentTitle.isNotEmpty()) {
      products.add(
        MarketplaceProduct(
          marketplace = currentMarketplace.ifEmpty { "Неизвестно" },
          productTitle = currentTitle,
          reference = currentReference.ifEmpty { "Ссылка не найдена" },
          rating = currentRating,
        ),
      )
    }

    // Ensure we have at least some products, create fallback if needed
    if (products.isEmpty()) {
      products.add(
        MarketplaceProduct(
          marketplace = "Результат не найден",
          productTitle = "Товары не найдены",
          reference = "Попробуйте другое изображение",
          rating = null,
        ),
      )
    }

    return products.take(5) // Limit to top 5 products
  }

  private fun detectImageFormat(imageBytes: ByteArray): String = when {
    imageBytes.size >= 4 && imageBytes[0] == 0xFF.toByte() && imageBytes[1] == 0xD8.toByte() -> "image/jpeg"
    imageBytes.size >= 8 && imageBytes[0] == 0x89.toByte() && imageBytes[1] == 0x50.toByte() &&
      imageBytes[2] == 0x4E.toByte() && imageBytes[3] == 0x47.toByte() -> "image/png"

    imageBytes.size >= 4 && imageBytes[0] == 0x47.toByte() && imageBytes[1] == 0x49.toByte() &&
      imageBytes[2] == 0x46.toByte() && imageBytes[3] == 0x38.toByte() -> "image/gif"

    imageBytes.size >= 4 && imageBytes[0] == 0x52.toByte() && imageBytes[1] == 0x49.toByte() &&
      imageBytes[2] == 0x46.toByte() && imageBytes[3] == 0x46.toByte() -> "image/webp"

    else -> "image/jpeg" // Default fallback
  }
}
