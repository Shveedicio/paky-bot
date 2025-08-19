package com.shveed.paky.bot.api.openai.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OpenAiRequest(
  @param:JsonProperty("model") val model: String,
  @param:JsonProperty("messages") val messages: List<Message>,
  @param:JsonProperty("max_tokens") val maxTokens: Int,
)
