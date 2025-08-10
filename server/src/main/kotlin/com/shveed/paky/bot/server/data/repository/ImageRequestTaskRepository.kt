package com.shveed.paky.bot.server.data.repository

import com.shveed.paky.bot.server.data.entity.ImageRequestTask
import org.springframework.data.domain.Limit
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ImageRequestTaskRepository : JpaRepository<ImageRequestTask, UUID> {
  fun findByStatus(status: ImageRequestTask.Status, limit: Limit): ImageRequestTask?
}
