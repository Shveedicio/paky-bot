package com.shveed.paky.bot.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.shveed.paky.bot.api.openai.OpenAIApi
import com.shveed.paky.bot.api.openai.model.ContentItem
import com.shveed.paky.bot.api.openai.model.ImageUrl
import com.shveed.paky.bot.api.openai.model.Message
import com.shveed.paky.bot.api.openai.model.OpenAiRequest
import com.shveed.paky.bot.server.config.props.AIProps
import com.shveed.paky.bot.server.constant.AiRequests.OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.util.Base64
import javax.imageio.ImageIO

private val log = KotlinLogging.logger {}

@Service
class OpenAIVisionService(private val openAIApi: OpenAIApi, private val aiProps: AIProps) {

  fun analyzeImage(imageBytes: ByteArray): String {
    // Validate image size (OpenAI has limits)
    if (imageBytes.size > 20 * 1024 * 1024) { // 20MB limit
      throw IllegalArgumentException("Image too large. Maximum size is 20MB")
    }

    val base64Image = Base64.getEncoder().encodeToString(imageBytes)

    // Validate base64 string
    if (base64Image.isEmpty()) {
      throw IllegalArgumentException("Base64 encoding failed - empty result")
    }

    log.info { "Base64 image length: ${base64Image.length}" }
    log.debug { "Base64 image preview: ${base64Image.take(100)}..." }

    // Detect image format
    val imageFormat = detectImageFormat(imageBytes)
    log.info { "Detected image format: $imageFormat" }

    // Validate base64 length (OpenAI has limits)
    if (base64Image.length > 4 * 1024 * 1024) { // 4MB base64 limit
      throw IllegalArgumentException("Base64 image too large. Maximum size is 4MB")
    }

    // Try with system message and proper content structure
    val requestBody = OpenAiRequest(
      model = aiProps.openAi.model,
      messages = listOf(
        Message(
          role = "system",
          content = listOf(
            ContentItem(
              type = "text",
              text = "You are a helpful assistant that analyzes product images and provides detailed descriptions.",
            ),
          ),
        ),
        Message(
          role = "user",
          content = listOf(
            ContentItem(type = "text", text = OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT),
            ContentItem(
              type = "image_url",
              image_url = ImageUrl(url = "data:$imageFormat;base64,$base64Image"),
            ),
          ),
        ),
      ),
      max_tokens = 300,
    )

    log.info { "Sending OpenAI Vision request with image size: ${imageBytes.size} bytes" }

    // Debug: Print the actual JSON that will be sent
    val objectMapper = ObjectMapper()
    val jsonString = objectMapper.writeValueAsString(requestBody)
    log.info { "Actual JSON being sent: $jsonString" }

    log.info { "Making HTTP request to OpenAI API..." }

    val response = openAIApi.analyzeMessageRequest(
      authorization = "Bearer ${aiProps.openAi.key}",
      requestBody = requestBody,
    )

    if (!response.statusCode.is2xxSuccessful) {
      log.error { "OpenAI API error: ${response.statusCode.value()}, body: ${response.body}" }
      log.error { "Request that failed: $requestBody" }
      throw RuntimeException("OpenAI API error: ${response.statusCode.value()} - ${response.body}")
    }

    log.info { "OpenAI API request successful, parsing response..." }
    return parseOpenAIResponse(response.body)
  }

  private fun parseOpenAIResponse(jsonResponse: String): String {
    // Парсинг ответа от OpenAI
    return jsonResponse
  }

  private fun detectImageFormat(imageBytes: ByteArray): String = try {
    // Check file signatures
    when {
      imageBytes.size >= 4 &&
        imageBytes[0] == 0xFF.toByte() &&
        imageBytes[1] == 0xD8.toByte() &&
        imageBytes[2] == 0xFF.toByte() -> "image/jpeg"

      imageBytes.size >= 8 &&
        imageBytes[0] == 0x89.toByte() &&
        imageBytes[1] == 0x50.toByte() &&
        imageBytes[2] == 0x4E.toByte() &&
        imageBytes[3] == 0x47.toByte() -> "image/png"

      imageBytes.size >= 6 &&
        imageBytes[0] == 0x47.toByte() &&
        imageBytes[1] == 0x49.toByte() &&
        imageBytes[2] == 0x46.toByte() -> "image/gif"

      else -> {
        // Try to read with ImageIO as fallback
        val inputStream = ByteArrayInputStream(imageBytes)
        val image = ImageIO.read(inputStream)

        when {
          image == null -> "image/jpeg" // Default fallback
          image.colorModel.hasAlpha() -> "image/png"
          else -> "image/jpeg"
        }
      }
    }
  } catch (e: Exception) {
    log.warn(e) { "Could not detect image format, defaulting to JPEG" }
    "image/jpeg"
  }
}
