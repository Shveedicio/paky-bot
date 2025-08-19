package com.shveed.paky.bot.server.config

import com.shveed.paky.bot.api.openai.OpenAIApi
import com.shveed.paky.bot.server.config.props.AIProps
import feign.okhttp.OkHttpClient
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.cloud.openfeign.FeignClientBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ImportAutoConfiguration(FeignAutoConfiguration::class)
class FeignConfig(private val aiProps: AIProps) {

  @Bean
  fun okHttpClient(): OkHttpClient = OkHttpClient()

  @Bean
  fun openAIApi(applicationContext: ApplicationContext): OpenAIApi = FeignClientBuilder(applicationContext)
    .forType(OpenAIApi::class.java, "openAIApi")
    .url(aiProps.openAi.url)
    .build()
}
