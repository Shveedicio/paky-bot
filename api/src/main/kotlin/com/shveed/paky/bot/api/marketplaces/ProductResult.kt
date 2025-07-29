package com.shveed.paky.bot.api.marketplaces

import java.math.BigDecimal

data class ProductResult(
  val id: String, // Уникальный идентификатор товара
  val title: String, // Название товара
  val url: String, // Ссылка на товар
  val marketplace: String, // "aliexpress", "ozon", "wildberries"
  val imageUrl: String? = null, // Главное изображение
  val images: List<String> = emptyList(), // Список всех изображений
  val price: BigDecimal? = null, // Текущая цена
  val originalPrice: BigDecimal? = null, // Первоначальная цена
  val discount: Int? = null, // Размер скидки в процентах
  val discountedPrice: BigDecimal? = null, // Цена со скидкой
  val rating: Double? = null, // Рейтинг товара
  val reviewsCount: Int? = null, // Количество отзывов
  val sellerName: String? = null, // Имя продавца
  val sellerUrl: String? = null, // Ссылка на магазин продавца
  val brand: String? = null, // Бренд
  val category: String? = null, // Категория
  val description: String? = null, // Описание
  val availability: Boolean? = null, // Наличие
  val vendorCode: String? = null, // Артикул продавца
  val commission: BigDecimal? = null, // Комиссия (для AliExpress)
  val deliveryOptions: List<String> = emptyList(), // Варианты доставки
  val specifications: Map<String, Any> = emptyMap(), // Характеристики товара
)
