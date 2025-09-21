package com.shveed.paky.bot.server.handler

import com.shveed.paky.bot.server.config.props.TelegramProps
import com.shveed.paky.bot.server.constant.TelegramMessages.COULD_NOT_PROCESS_IMAGE
import com.shveed.paky.bot.server.constant.TelegramMessages.ON_IMAGE_ACCEPTED
import com.shveed.paky.bot.server.constant.TelegramMessages.ON_OTHER_TEXT_SENT
import com.shveed.paky.bot.server.constant.TelegramMessages.ON_START_COMMAND
import com.shveed.paky.bot.server.constant.TelegramMessages.UNKNOWN_ACTION_REQUESTED
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

      else -> handleUnknownAction(update.message)
    }
  }

  private fun handlePhotoMessage(message: Message) {
    val chatId = message.chatId
    try {
      imageProcessingService.processImage(message, telegramProps.token)
      sendMessage(chatId, ON_IMAGE_ACCEPTED)
    } catch (ex: Exception) {
      log.error(ex) { "Error processing photo" }
      sendMessage(chatId, COULD_NOT_PROCESS_IMAGE)
    }
  }

  private fun handleTextMessage(message: Message) {
    if (message.text.equals(COMMAND_START, ignoreCase = true)) {
      sendMessage(message.chatId, ON_START_COMMAND)
    } else {
      sendMessage(message.chatId, ON_OTHER_TEXT_SENT)
    }
  }

  private fun handleUnknownAction(message: Message) {
    log.info { "Unknown telegram action received" }
    sendMessage(message.chatId, UNKNOWN_ACTION_REQUESTED.trimIndent())
  }

  fun sendMessage(chatId: Long, text: String) {
    try {
      execute(
        SendMessage(chatId.toString(), text).apply {
          enableMarkdown(true)
        },
      )
    } catch (e: TelegramApiException) {
      log.error(e) { "Failed to send message" }
    }
  }

  fun downloadImageFile(imageId: String): ByteArray {
    // 1. Получаем FilePath через метод getFile
    val getFile = GetFile().apply { this.fileId = imageId }
    val telegramFile = execute(getFile) // может бросить TelegramApiException
    val filePath = telegramFile.filePath
      ?: throw IllegalStateException("Telegram didn't return file path for id=$imageId")

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

  companion object {
    private const val COMMAND_START = "/start"
  }
}
