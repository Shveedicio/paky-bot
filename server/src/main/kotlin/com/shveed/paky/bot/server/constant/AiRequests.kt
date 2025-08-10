package com.shveed.paky.bot.server.constant

object AiRequests {
  const val OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT =
    """
			Describe this product for marketplace search. Focus on key characteristics.
			Provide answer as a list of strings, where each element contains 1-3 words that can be represented
			as list of tags for marketplace search. Answer in Russian language.
    """
}
