package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.api.perplexity.PerplexityApi
import com.shveed.paky.bot.api.perplexity.model.PerplexityContentItem
import com.shveed.paky.bot.api.perplexity.model.PerplexityMessage
import com.shveed.paky.bot.api.perplexity.model.PerplexityRequest
import com.shveed.paky.bot.server.config.props.AIProps
import com.shveed.paky.bot.server.constant.AiRequests.PERPLEXITY_PRODUCT_SEARCH_REQUEST_TEXT
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class PerplexityVisionService(private val perplexityApi: PerplexityApi, private val aiProps: AIProps) {

  fun searchProductsBasedOnDescription(productDescription: String): String {
    val requestBody = PerplexityRequest(
      model = aiProps.perplexity.model,
      messages = listOf(
        PerplexityMessage(
          role = "user",
          content = listOf(
            PerplexityContentItem(
              type = "text",
              text = "$PERPLEXITY_PRODUCT_SEARCH_REQUEST_TEXT\n\nОписание товара: $productDescription",
            ),
          ),
        ),
      ),
      maxTokens = 1000,
      temperature = 0.7,
    )

    val response = perplexityApi.analyzeImageAndSearchProducts(
      authorization = "Bearer ${aiProps.perplexity.key}",
      requestBody = requestBody,
    )

    if (!response.statusCode.is2xxSuccessful) {
      log.error { response.body }
      throw RuntimeException("Perplexity API error: ${response.statusCode.value()}")
    }

    return parsePerplexityResponse(response.body)
  }

  private fun parsePerplexityResponse(jsonResponse: String): String {
    // Parse Perplexity response and extract product information
    // Perplexity should return structured data about found products
    return jsonResponse
  }
}
