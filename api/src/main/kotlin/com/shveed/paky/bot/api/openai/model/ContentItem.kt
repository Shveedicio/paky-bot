package com.shveed.paky.bot.api.openai.model

data class ContentItem(
  val type: String,
  val text: String? = null,
  val image_url: ImageUrl? = null,
)
