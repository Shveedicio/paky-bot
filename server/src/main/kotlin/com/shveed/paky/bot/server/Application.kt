package com.shveed.paky.bot.server

import com.shveed.paky.bot.server.config.props.TelegramProps
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableAsync
import java.util.TimeZone

@SpringBootApplication
@EnableConfigurationProperties(
  TelegramProps::class,
)
@EnableAsync
@EnableFeignClients
class Application

fun main(args: Array<String>) {
  TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
  runApplication<Application>(*args)
}
