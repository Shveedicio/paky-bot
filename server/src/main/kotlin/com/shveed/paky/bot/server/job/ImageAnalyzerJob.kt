package com.shveed.paky.bot.server.job

import com.shveed.paky.bot.server.data.entity.ImageRequestTask
import com.shveed.paky.bot.server.data.repository.ImageRequestTaskRepository
import com.shveed.paky.bot.server.service.ImageAnalysingService
import org.springframework.data.domain.Limit
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ImageAnalyzerJob(
  private val imageAnalysingService: ImageAnalysingService,
  private val imageRequestTaskRepository: ImageRequestTaskRepository,
) {

  @Scheduled(fixedDelay = 5000)
  @Transactional
  fun run() {
    val imageRequestTask =
      imageRequestTaskRepository.findByStatus(
        ImageRequestTask.Status.CREATED,
        Limit.of(1),
      ) ?: return

    imageAnalysingService.analyzeImage(imageRequestTask)
  }
}
