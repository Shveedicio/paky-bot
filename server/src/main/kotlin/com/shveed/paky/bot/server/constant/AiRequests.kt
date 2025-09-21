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
			Проанализируй это изображение товара и найди ТОЧНО 5 лучших похожих товаров на российских маркетплейсах (Ozon, Wildberries, Yandex Market).

			Для каждого из 5 товаров предоставь:
			- Название товара
			- Краткое описание
			- Название маркетплейса (Ozon/Wildberries/Yandex Market)
			- Диапазон цен в рублях
			- Категорию товара
			- Ключевые особенности
			- Ссылку на товар (если доступна)

			Сосредоточься на товарах, которые:
			- Сейчас доступны для покупки
			- Популярны в России
			- Максимально похожи на изображенный товар

			Предоставь ответ на русском языке.
			Форматируй ответ как пронумерованный список от 1 до 5 с четкими рекомендациями товаров.
			В ответе перечисли только список ссылок на товары.
			Не предоставляй другую информацию.
    """
}
