package com.shveed.paky.bot.server.service

import com.shveed.paky.bot.server.data.entity.ImageRequestTask
import com.shveed.paky.bot.server.data.repository.ImageSearchRepository
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

@Service
class ImageProcessingService(private val imageSearchRepository: ImageSearchRepository) {

  fun downloadPhotoFile(fileId: String): ByteArray {
    // 1. Получаем FilePath через метод getFile
    val getFile = GetFile()
    getFile.fileId = fileId
    val telegramFile = execute(getFile) // может бросить TelegramApiException
    val filePath = telegramFile.filePath
      ?: throw IllegalStateException("Telegram didn't return file path for id=$fileId")

    // 2. Формируем URL вида https://api.telegram.org/file/bot<token>/<file_path>
    val fileUrl = "https://api.telegram.org/file/bot${telegramProps.token}/$filePath"

    // 3. Скачиваем файл через стандартный HttpClient
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
      .uri(URI.create(fileUrl))
      .GET()
      .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofByteArray())
    if (response.statusCode() != 200) {
      throw TelegramApiException("Failed to load image, status=${response.statusCode()}")
    }
    return response.body()
  }

  @Async
  fun processImage(userId: Long, imageBytes: ByteArray): CompletableFuture<ImageRequestTask> {
// 		val description = visionApiService.describeImage(imageBytes)
// 		val searchResults = marketplaceService.searchAllMarketplaces(description)

// 		val imageSearch = ImageSearch(
// 			userId = userId,
// 			description = description,
// 			searchResults = searchResults
// 		)
    TODO()
// 		return CompletableFuture.completedFuture(imageSearchRepository.save(imageSearch))
  }
}
