package com.shveed.paky.bot.api.perplexity

import com.shveed.paky.bot.api.perplexity.model.PerplexityRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

interface PerplexityApi {

  @PostMapping(
    value = ["/chat/completions"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  fun analyzeImageAndSearchProducts(
    @RequestHeader("Authorization") authorization: String,
    @RequestBody requestBody: PerplexityRequest,
  ): ResponseEntity<String>
}
