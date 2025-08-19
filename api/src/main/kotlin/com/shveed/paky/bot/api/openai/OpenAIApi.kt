package com.shveed.paky.bot.api.openai

import com.shveed.paky.bot.api.openai.model.OpenAiRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

interface OpenAIApi {

  @PostMapping("/v1/chat/completions")
  fun analyzeMessageRequest(
    @RequestHeader("Authorization") authorization: String,
    @RequestHeader("Content-Type") contentType: String = "application/json",
    @RequestBody requestBody: OpenAiRequest,
  ): ResponseEntity<String>
}
