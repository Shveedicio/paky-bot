package com.shveed.paky.bot.server.job

import com.shveed.paky.bot.server.data.entity.ImageRequestTask
import com.shveed.paky.bot.server.data.repository.ImageRequestTaskRepository
import com.shveed.paky.bot.server.service.ResponseGenerationService
import org.springframework.data.domain.Limit
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ResponseGenerationJob(
  private val responseGenerationService: ResponseGenerationService,
  private val imageRequestTaskRepository: ImageRequestTaskRepository,
) {

  @Scheduled(fixedDelay = 5000)
  @Transactional
  fun run() {
    val imageRequestTask =
      imageRequestTaskRepository.findByStatus(
        ImageRequestTask.Status.MARKETPLACE_ANALYSIS,
        Limit.of(1),
      ) ?: return

    responseGenerationService.generateAndSendResponse(imageRequestTask)
  }
}
