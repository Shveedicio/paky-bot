package com.shveed.paky.bot.server.constant

object AiRequests {
  const val OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT =
    """
			Describe this product for marketplace search. Focus on key characteristics.
			Provide answer as a list of strings, where each element contains 1-3 words that can be represented
			as list of tags for marketplace search. Answer in Russian language.
    """

  const val PERPLEXITY_IMAGE_ANALYSIS_REQUEST_TEXT =
    """
			Analyze this product image and find the 5 best matching products on Russian marketplaces (Ozon, Wildberries, Yandex Market).
			For each product, provide:
			- Product name and description
			- Marketplace name
			- Estimated price range
			- Product category
			- Key features

			Focus on products that are currently available and popular in Russia.
			Provide your response in Russian language.
			Format the response as a structured list with clear product recommendations.
    """
}
