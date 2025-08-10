package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.server.data.entity.ImageRequestTask
import com.shveed.paky.bot.server.data.repository.ImageRequestTaskRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

private val log = KotlinLogging.logger {}

@Service
class ImageProcessingService(private val imageRequestTaskRepository: ImageRequestTaskRepository) {
  fun processImage(message: Message, botToken: String) {
    ImageRequestTask(
      telegramId = message.from.id,
      status = ImageRequestTask.Status.CREATED,
      chatId = message.chatId,
      messageId = message.messageId,
      imageId = message
        .photo
        .maxByOrNull { it.fileSize }
        ?.fileId
        ?: run {
          log.error { "Error while trying to pick up requested image" }
          return
        },
    ).also(imageRequestTaskRepository::saveAndFlush)
  }
}
