package com.shveed.paky.bot.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.Optional

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories("com.shveed.paky.bot.server.data.repository")
@EnableTransactionManagement
class DatabaseConfig {

  @Bean
  fun auditorAware(): AuditorAware<String> = AuditorAware<String> { Optional.of("SYSTEM") }
}
