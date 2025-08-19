package com.shveed.paky.bot.api.openai.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ContentItem(
  @param:JsonProperty("type") val type: String,
  @param:JsonProperty("text") val text: String? = null,
  @param:JsonProperty("image_url") val imageUrl: ImageUrl? = null,
)
