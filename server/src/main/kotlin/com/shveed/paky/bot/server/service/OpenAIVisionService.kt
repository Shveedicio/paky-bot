package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.api.marketplaces.openai.OpenAIApi
import com.shveed.paky.bot.api.marketplaces.openai.model.ContentItem
import com.shveed.paky.bot.api.marketplaces.openai.model.ImageUrl
import com.shveed.paky.bot.api.marketplaces.openai.model.Message
import com.shveed.paky.bot.api.marketplaces.openai.model.OpenAiRequest
import com.shveed.paky.bot.server.config.props.AIProps
import com.shveed.paky.bot.server.constant.AiRequests.OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.util.Base64

private val log = KotlinLogging.logger {}

@Service
class OpenAIVisionService(private val openAIApi: OpenAIApi, private val aiProps: AIProps) {

  fun analyzeImage(imageBytes: ByteArray): String {
    val base64Image = Base64.getEncoder().encodeToString(imageBytes)

    val requestBody = OpenAiRequest(
      model = aiProps.openAi.model,
      messages = listOf(
        Message(
          role = "user",
          content = listOf(
            ContentItem(type = "text", text = OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT),
            ContentItem(
              type = "image_url",
              imageUrl = ImageUrl(url = "data:image/jpeg;base64,$base64Image"),
            ),
          ),
        ),
      ),
      maxTokens = 300,
    )

    val response = openAIApi.analyzeMessageRequest(
      authorization = "Bearer ${aiProps.openAi.key}",
      requestBody = requestBody,
    )

    if (!response.statusCode.is2xxSuccessful) {
      log.error { response.body }
      throw RuntimeException("OpenAI API error: ${response.statusCode.value()}")
    }

    return parseOpenAIResponse(response.body)
  }

  private fun parseOpenAIResponse(jsonResponse: String): String {
    // Парсинг ответа от OpenAI
    return jsonResponse
  }
}
