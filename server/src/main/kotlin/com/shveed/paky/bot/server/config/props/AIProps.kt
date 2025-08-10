package com.shveed.paky.bot.server.config.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "ai")
data class AIProps(var openAi: OpenAIBlock, var azureVision: AzureBlock) {
  data class OpenAIBlock(var key: String, var url: String, var model: String)

  data class AzureBlock(var endpoint: String, var key: String)
}
