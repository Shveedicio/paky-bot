package com.shveed.paky.bot.api.marketplaces.openai.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OpenAiRequest(
  @JsonProperty("model") val model: String,
  @JsonProperty("messages") val messages: List<Message>,
  @JsonProperty("max_tokens") val maxTokens: Int,
)
