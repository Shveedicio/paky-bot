package com.shveed.paky.bot.server.constant

object TelegramMessages {
  const val ON_IMAGE_ACCEPTED = "📸 Фото получено, ищу товары…"
  const val COULD_NOT_PROCESS_IMAGE = "❌ Не удалось обработать изображение. Попробуйте ещё раз."
  const val ON_START_COMMAND = "👋 Отправьте фотографию товара, а я найду его на маркетплейсах!"
  const val ON_OTHER_TEXT_SENT = "📷 Пожалуйста, пришлите фото товара."
  const val UNKNOWN_ACTION_REQUESTED = """
				Вероятно вы прислали такой формат изображения, с которым я пока не умею работать =(
				Следите за новостями. Когда-нибудь я смогу...
  """
  const val REQUEST_ACCEPTED = "Ваш запрос принят и отправлен на обработку. Скоро подберу для вас несколько товаров..."
}
