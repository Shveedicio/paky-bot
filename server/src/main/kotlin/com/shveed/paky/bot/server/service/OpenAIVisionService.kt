package com.shveed.paky.bot.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.shveed.paky.bot.server.config.props.AIProps
import com.shveed.paky.bot.server.constant.AiRequests.OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.Base64

private val log = KotlinLogging.logger {}

@Service
class OpenAIVisionService(private val aiProps: AIProps, private val objectMapper: ObjectMapper) {

  private val client = HttpClient.newBuilder().build()

  fun analyzeImage(imageBytes: ByteArray): String {
    val base64Image = Base64.getEncoder().encodeToString(imageBytes)

    val requestBody = OpenAIRequest(
      model = "gpt-4o",
      messages = listOf(
        Message(
          role = "user",
          content = listOf(
            ContentItem(type = "text", text = OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT),
            ContentItem(
              type = "image_url",
              image_url = ImageUrl(url = "data:image/jpeg;base64,$base64Image"),
            ),
          ),
        ),
      ),
      max_tokens = 300,
    )

    val request = HttpRequest.newBuilder()
      .uri(URI.create(aiProps.openAi.url))
      .header("Authorization", "Bearer ${aiProps.openAi.key}")
      .header("Content-Type", "application/json")
      .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
      .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    if (response.statusCode() != 200) {
      log.error { response.body() }
      throw RuntimeException("OpenAI API error: ${response.statusCode()}")
    }

    return parseOpenAIResponse(response.body())
  }

  private fun parseOpenAIResponse(jsonResponse: String): String {
    // Парсинг ответа от OpenAI
    return ""
  }

  data class OpenAIRequest(val model: String, val messages: List<Message>, val max_tokens: Int)

  data class Message(val role: String, val content: List<ContentItem>)

  data class ContentItem(val type: String, val text: String? = null, val image_url: ImageUrl? = null)

  data class ImageUrl(val url: String)
}
