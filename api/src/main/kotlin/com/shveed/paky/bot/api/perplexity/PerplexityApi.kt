package com.shveed.paky.bot.api.perplexity

import com.shveed.paky.bot.api.perplexity.model.PerplexityRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

interface PerplexityApi {

  @PostMapping("/v1/chat/completions")
  fun analyzeImageAndSearchProducts(
    @RequestHeader("Authorization") authorization: String,
    @RequestHeader("Content-Type") contentType: String = "application/json",
    @RequestBody requestBody: PerplexityRequest,
  ): ResponseEntity<String>
}
