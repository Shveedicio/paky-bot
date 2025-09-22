package com.shveed.paky.bot.api.perplexity.model

data class PerplexityRequest(
  val model: String,
  val messages: List<PerplexityMessage>,
  val maxTokens: Int? = null,
  val temperature: Double? = null,
)

data class PerplexityMessage(val role: String, val content: String)

data class PerplexityContentItem(val type: String, val text: String? = null, val imageUrl: PerplexityImageUrl? = null)

data class PerplexityImageUrl(val url: String)
