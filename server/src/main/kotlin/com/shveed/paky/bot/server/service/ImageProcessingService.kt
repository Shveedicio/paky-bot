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

//  fun downloadPhotoFile(fileId: String): ByteArray {
//    // 1. Получаем FilePath через метод getFile
//    val getFile = GetFile()
//    getFile.fileId = fileId
//    val telegramFile = execute(getFile) // может бросить TelegramApiException
//    val filePath = telegramFile.filePath
//      ?: throw IllegalStateException("Telegram didn't return file path for id=$fileId")
//
//    // 2. Формируем URL вида https://api.telegram.org/file/bot<token>/<file_path>
//    val fileUrl = "https://api.telegram.org/file/bot${telegramProps.token}/$filePath"
//
//    // 3. Скачиваем файл через стандартный HttpClient
//    val client = HttpClient.newBuilder().build()
//    val request = HttpRequest.newBuilder()
//      .uri(URI.create(fileUrl))
//      .GET()
//      .build()
//
//    val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())
//    if (response.statusCode() != 200) {
//      throw TelegramApiException("Failed to load image, status=${response.statusCode()}")
//    }
//    return response.body()
//  }
}
