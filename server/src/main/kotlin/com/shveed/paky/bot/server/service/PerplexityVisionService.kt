package com.shveed.paky.bot.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.shveed.paky.bot.api.perplexity.PerplexityApi
import com.shveed.paky.bot.api.perplexity.model.PerplexityContentItem
import com.shveed.paky.bot.api.perplexity.model.PerplexityImageUrl
import com.shveed.paky.bot.api.perplexity.model.PerplexityMessage
import com.shveed.paky.bot.api.perplexity.model.PerplexityRequest
import com.shveed.paky.bot.server.config.props.AIProps
import com.shveed.paky.bot.server.constant.AiRequests.PERPLEXITY_PRODUCT_SEARCH_REQUEST_TEXT
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.util.Base64

private val log = KotlinLogging.logger {}

@Service
class PerplexityVisionService(private val perplexityApi: PerplexityApi, private val aiProps: AIProps) {

  fun searchProductsBasedOnImage(imageBytes: ByteArray): String {
    // Validate image size
    if (imageBytes.size > 20 * 1024 * 1024) { // 20MB limit
      throw IllegalArgumentException("Image too large. Maximum size is 20MB")
    }

    val base64Image = Base64.getEncoder().encodeToString(imageBytes)

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
        imageUrl = PerplexityImageUrl(url = "data:$imageFormat;base64,$base64Image"),
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

  private fun parsePerplexityResponse(jsonResponse: String): String {
    // Parse Perplexity response and extract product information
    // Perplexity should return structured data about found products
    return jsonResponse
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
