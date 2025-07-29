package com.shveed.paky.bot.server.config.props

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "telegram.bot")
class TelegramProps(var token: String, var username: String)
