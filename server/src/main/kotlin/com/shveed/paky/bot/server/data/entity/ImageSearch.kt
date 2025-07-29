package com.shveed.paky.bot.server.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "image_search")
data class ImageSearch(
  @Id @GeneratedValue val id: UUID? = null,
  val userId: Long,
  val description: String,
  //  val searchResults: Map<String, List<ProductResult>>,
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  val createdAt: LocalDateTime = LocalDateTime.now(),
) {
// 	data class ProductResult()
}
