package com.shveed.paky.bot.server.handler

import com.shveed.paky.bot.server.config.props.TelegramProps
import com.shveed.paky.bot.server.service.ImageProcessingService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class TelegramBotMessageHandler(
  private val telegramProps: TelegramProps,
  private val imageProcessingService: ImageProcessingService,
) : TelegramLongPollingBot(telegramProps.token) {
  private val log = KotlinLogging.logger(javaClass.name)

  override fun getBotUsername(): String = telegramProps.username

  override fun onUpdateReceived(update: Update) {
    when {
      update.hasMessage() && update.message.hasPhoto() ->
        handlePhotoMessage(update.message)

      update.hasMessage() && update.message.hasText() ->
        handleTextMessage(update.message)

      else -> log.info { "Unknown telegram action received" }
    }
  }

  private fun handlePhotoMessage(message: Message) {
    val chatId = message.chatId
    try {
      val photo = message.photo.maxByOrNull { it.fileSize } ?: return
      val photoFileAsByteArray = downloadPhotoFile(photo.fileId)

      sendMessage(chatId, "📸 Фото получено, ищу товары…")

      processImageAsync(message, photoFileAsByteArray, chatId)
    } catch (ex: Exception) {
      log.error(ex) { "Error processing photo" }
      sendMessage(chatId, "❌ Не удалось обработать изображение. Попробуйте ещё раз.")
    }
  }

  private fun processImageAsync(message: Message, photoFileAsByteArray: ByteArray, chatId: Long) {
// 		imageProcessingService.processImage(message.from.id, photoFileAsByteArray).thenAccept { search ->
// 			val response = buildString {
// 				appendLine("🔍 Результаты поиска («${search.description}»):")
// 				appendLine()
// 				search.searchResults.forEach { (marketplace, products) ->
// 					appendLine("🛒 $marketplace:")
// 					products.take(3).forEach { p ->
// 						append("• ${p.title} — ${p.price ?: "n/a"} ")
// 						appendLine(p.url)
// 					}
// 					appendLine()
// 				}
// 			}
// 			sendMessage(chatId, response)
// 		}
  }

  private fun handleTextMessage(message: Message) {
    if (message.text.equals(COMMAND_START, ignoreCase = true)) {
      sendMessage(message.chatId, "👋 Отправьте фотографию товара, а я найду его на маркетплейсах!")
    } else {
      sendMessage(message.chatId, "📷 Пожалуйста, пришлите фото товара.")
    }
  }

  private fun downloadPhotoFile(fileId: String): ByteArray {
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

  private fun sendMessage(chatId: Long, text: String) {
    try {
      execute(
        SendMessage(chatId.toString(), text).apply {
          enableMarkdown(true)
        },
      )
    } catch (e: TelegramApiException) {
      log.error("Failed to send message", e)
    }
  }

  companion object {
    private const val COMMAND_START = "/start"
  }
}
