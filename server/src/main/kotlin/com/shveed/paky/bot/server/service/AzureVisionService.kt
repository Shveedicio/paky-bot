package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.server.config.props.AIProps
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class AzureVisionService(private val aIProps: AIProps) {
  private val client = HttpClient.newBuilder().build()

  fun analyzeImage(imageBytes: ByteArray): String {
    val endpoint = aIProps.azureVision.endpoint
    val subscriptionKey = aIProps.azureVision.key

    val request = HttpRequest.newBuilder()
      .uri(URI.create("$endpoint/vision/v3.1/analyze?visualFeatures=Categories,Description,Objects"))
      .header("Ocp-Apim-Subscription-Key", subscriptionKey)
      .header("Content-Type", "application/octet-stream")
      .POST(HttpRequest.BodyPublishers.ofByteArray(imageBytes))
      .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())

    if (response.statusCode() != 200) {
      throw RuntimeException("Azure Vision API error: ${response.statusCode()}")
    }

    return parseAzureResponse(response.body())
  }

  private fun parseAzureResponse(jsonResponse: String): String {
    // Парсинг JSON ответа от Azure и извлечение описания
    // Возвращаем описание товара для поиска
    return ""
  }
}
