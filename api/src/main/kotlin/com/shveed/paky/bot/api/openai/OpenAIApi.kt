package com.shveed.paky.bot.api.openai

import com.shveed.paky.bot.api.openai.model.OpenAiRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

interface OpenAIApi {

  @PostMapping(
    value = ["/v1/chat/completions"],
    consumes = [MediaType.APPLICATION_JSON_VALUE],
    produces = [MediaType.APPLICATION_JSON_VALUE],
  )
  fun analyzeMessageRequest(
    @RequestHeader("Authorization") authorization: String,
    @RequestHeader("Content-Type") contentType: String = "application/json",
    @RequestBody requestBody: OpenAiRequest,
  ): ResponseEntity<String>
}
