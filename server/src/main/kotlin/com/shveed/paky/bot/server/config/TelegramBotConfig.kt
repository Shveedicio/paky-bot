package com.shveed.paky.bot.server.config

import com.shveed.paky.bot.server.handler.TelegramBotMessageHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession

@Configuration
class TelegramBotConfig {

  @Bean
  fun telegramBotsApi(telegramBotMessageHandler: TelegramBotMessageHandler): TelegramBotsApi =
    TelegramBotsApi(DefaultBotSession::class.java).apply {
      registerBot(telegramBotMessageHandler)
    }
}
