package com.shveed.paky.bot.api.openai.model

data class OpenAiRequest(
  val model: String,
  val messages: List<Message>,
  val max_tokens: Int,
)
