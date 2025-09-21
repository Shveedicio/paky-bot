# paky-bot

A Telegram bot that analyzes product images and finds matching products on Russian marketplaces using Perplexity AI.

## Features

- **Image Analysis**: Receives product images via Telegram chat
- **AI-Powered Search**: Uses Perplexity API to analyze images and find matching products
- **Multi-Marketplace Support**: Searches across Ozon, Wildberries, and Yandex Market
- **Top 5 Results**: Returns the best 5 matching products with details
- **Asynchronous Processing**: Background jobs handle image processing and response generation

## Architecture

### Core Components

1. **TelegramBotMessageHandler**: Handles incoming messages and image uploads
2. **ImageProcessingService**: Creates processing tasks for uploaded images
3. **PerplexityVisionService**: Analyzes images using Perplexity API
4. **MarketPlaceSearchingService**: Searches products across multiple marketplaces
5. **ResponseGenerationService**: Formats and sends results to users

### Background Jobs

- **ImageAnalyzerJob**: Processes images with Perplexity API
- **ResponseGenerationJob**: Generates and sends responses to users

### Data Flow

1. User sends image → TelegramBotMessageHandler
2. ImageProcessingService creates task → Database
3. ImageAnalyzerJob picks up task → PerplexityVisionService analyzes image
4. ResponseGenerationJob formats response → Sends to user

## Configuration

### Required API Keys

```yaml
ai:
  perplexity:
    key: your-perplexity-api-key
    url: https://api.perplexity.ai
    model: llama-3.1-sonar-large-128k-online

api:
  ozon: 
    url: https://api.ozon.ru
    key: your-ozon-api-key
  wildberries: 
    url: https://suppliers-api.wildberries.ru
    key: your-wildberries-api-key
  yandex-market:
    url: https://api.partner.market.yandex.ru
    key: your-yandex-market-api-key

telegram:
  bot:
    token: your-telegram-bot-token
    username: your-bot-username
```

## Marketplace APIs

### Ozon
- Search endpoint: `/products/search`
- Documentation: https://docs.ozon.ru/api/

### Wildberries
- Search endpoint: `/search`
- Documentation: https://suppliers-api.wildberries.ru/

### Yandex Market
- Search endpoint: `/search`
- Documentation: https://yandex.ru/dev/market/

## AI Integration

### Perplexity API
- **Model**: `llama-3.1-sonar-large-128k-online`
- **Capabilities**: Image analysis + real-time web search
- **Use Case**: Analyzes product images and finds matching products on marketplaces

### Prompt Engineering
The bot uses specialized prompts to:
- Analyze product images
- Extract key product characteristics
- Search for matching products on Russian marketplaces
- Format responses in Russian language

## Database Schema

### ImageRequestTask Entity
- `id`: UUID primary key
- `telegramId`: User's Telegram ID
- `chatId`: Chat ID for responses
- `messageId`: Original message ID
- `imageId`: Telegram file ID
- `payload`: AI analysis results
- `status`: Processing status (CREATED, IMAGE_PROCESSING, MARKETPLACE_ANALYSIS, GENERATING_RESPONSE, SUCCESS, ERROR)
- `attemptCounter`: Retry counter for failed requests

## Development

### Prerequisites
- Java 17+
- Kotlin
- Spring Boot
- PostgreSQL
- Gradle

### Running the Application
```bash
./gradlew bootRun
```

### Testing
```bash
./gradlew test
```

## Future Enhancements

1. **User Management**: Add consumer validation and usage limits
2. **Subscription System**: Implement premium features
3. **Context Awareness**: Maintain chat context for better user experience
4. **Product Comparison**: Compare products across marketplaces
5. **Price Tracking**: Track price changes over time
6. **Favorites**: Allow users to save favorite products
7. **Notifications**: Alert users about price drops

## Links

### Marketplaces
- Ozon search: https://www.ozon.ru/search/?from_global=true&text=розовая+кофта
- Wildberries search: https://www.wildberries.ru/catalog/0/search.aspx?search=розовая%20кофта
- Yandex Market: https://market.yandex.ru/

### AI Services
- Perplexity API: https://docs.perplexity.ai/api
- OpenAI Vision (fallback): https://platform.openai.com/docs/guides/vision
