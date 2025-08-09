package com.shveed.paky.bot.server.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "image_request_task")
data class ImageRequestTask(
  @Id
  @GeneratedValue
  val id: UUID? = null,
  @Column(name = "telegram_id")
  val telegramId: Long,
  @Column(name = "chat_id")
  val chatId: Long,
  @Column(name = "message_id")
  val messageId: Int,
  @Column(name = "image_id")
  val imageId: String,
  @Column(name = "payload", columnDefinition = "text")
  val payload: String? = null,
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  val status: Status,
//  @Column(columnDefinition = "text")
//  val searchResults: Map<String, List<ProductResult>>,
) : Auditable() {
  enum class Status {
    CREATED,
    IMAGE_PROCESSING,
    MARKETPLACE_ANALYSIS,
    GENERATING_RESPONSE,
    SUCCESS,
    ERROR,
  }

// 	data class ProductResult()
}
