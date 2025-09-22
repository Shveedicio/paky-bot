package com.shveed.paky.bot.api.perplexity.model

import com.fasterxml.jackson.annotation.JsonProperty

data class PerplexityResponse(
  val id: String,
  val `object`: String,
  val created: Long,
  val model: String,
  val choices: List<PerplexityChoice>,
  val usage: PerplexityUsage? = null,
)

data class PerplexityChoice(
  val index: Int,
  val message: PerplexityResponseMessage,
  val finishReason: String? = null,
)

data class PerplexityResponseMessage(
  val role: String,
  val content: String,
)

data class PerplexityUsage(
  @JsonProperty("prompt_tokens")
  val promptTokens: Int,
  @JsonProperty("completion_tokens")
  val completionTokens: Int,
  @JsonProperty("total_tokens")
  val totalTokens: Int,
)
