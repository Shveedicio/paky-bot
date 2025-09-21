package com.shveed.paky.bot.server.config

import com.shveed.paky.bot.api.marketplaces.ozon.OzonApi
import com.shveed.paky.bot.api.marketplaces.wildberries.WildberriesApi
import com.shveed.paky.bot.api.marketplaces.yandexmarket.YandexMarketApi
import com.shveed.paky.bot.api.openai.OpenAIApi
import com.shveed.paky.bot.api.perplexity.PerplexityApi
import com.shveed.paky.bot.server.config.props.AIProps
import com.shveed.paky.bot.server.config.props.ApiProps
import feign.okhttp.OkHttpClient
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.cloud.openfeign.FeignClientBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ImportAutoConfiguration(FeignAutoConfiguration::class)
class FeignConfig(private val aiProps: AIProps, private val apiProps: ApiProps) {

  @Bean
  fun okHttpClient(): OkHttpClient = OkHttpClient()

  @Bean
  fun openAIApi(applicationContext: ApplicationContext): OpenAIApi = FeignClientBuilder(applicationContext)
    .forType(OpenAIApi::class.java, "openAIApi")
    .url(aiProps.openAi.url)
    .build()

  @Bean
  fun perplexityApi(applicationContext: ApplicationContext): PerplexityApi = FeignClientBuilder(applicationContext)
    .forType(PerplexityApi::class.java, "perplexityApi")
    .url(aiProps.perplexity.url)
    .build()

  @Bean
  fun ozonApi(applicationContext: ApplicationContext): OzonApi = FeignClientBuilder(applicationContext)
    .forType(OzonApi::class.java, "ozonApi")
    .url(apiProps.ozon.url)
    .build()

  @Bean
  fun wildberriesApi(applicationContext: ApplicationContext): WildberriesApi = FeignClientBuilder(applicationContext)
    .forType(WildberriesApi::class.java, "wildberriesApi")
    .url(apiProps.wildberries.url)
    .build()

  @Bean
  fun yandexMarketApi(applicationContext: ApplicationContext): YandexMarketApi = FeignClientBuilder(applicationContext)
    .forType(YandexMarketApi::class.java, "yandexMarketApi")
    .url(apiProps.yandexMarket.url)
    .build()
}
