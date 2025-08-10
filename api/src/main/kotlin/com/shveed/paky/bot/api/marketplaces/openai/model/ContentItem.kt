package com.shveed.paky.bot.api.marketplaces.openai.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ContentItem(
  @JsonProperty("type") val type: String,
  @JsonProperty("text") val text: String? = null,
  @JsonProperty("image_url") val imageUrl: ImageUrl? = null,
)
