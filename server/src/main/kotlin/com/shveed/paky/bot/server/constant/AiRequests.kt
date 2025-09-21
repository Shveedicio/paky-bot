package com.shveed.paky.bot.server.constant

object AiRequests {
  const val OPEN_AI_IMAGE_DESCRIPTION_REQUEST_TEXT =
    """
			Describe this product for marketplace search. Focus on key characteristics.
			Provide answer as a list of strings, where each element contains 1-3 words that can be represented
			as list of tags for marketplace search. Answer in Russian language.
    """

  const val PERPLEXITY_PRODUCT_SEARCH_REQUEST_TEXT =
    """
			Найди 5 лучших товаров на российских маркетплейсах (Ozon, Wildberries, Yandex Market) по данному описанию.
			Для каждого товара предоставь:
			- Название и описание товара
			- Название маркетплейса
			- Диапазон цен
			- Категорию товара
			- Ключевые особенности
			
			Сосредоточься на товарах, которые сейчас доступны и популярны в России.
			Предоставь ответ на русском языке.
			Форматируй ответ как структурированный список с четкими рекомендациями товаров.
    """
}
