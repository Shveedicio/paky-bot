package com.shveed.paky.bot.server.data.repository

import com.shveed.paky.bot.server.data.entity.ImageSearch
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ImageSearchRepository : JpaRepository<ImageSearch, UUID>
