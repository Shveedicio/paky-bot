# paky-bot

A Telegram bot that analyzes product images and finds matching products on Russian marketplaces using a hybrid AI approach combining OpenAI Vision and Perplexity API.

## Features

- **Image Analysis**: Receives product images via Telegram chat
- **Hybrid AI Approach**: Combines OpenAI Vision for image analysis with Perplexity API for product search
- **Multi-Marketplace Support**: Searches across Ozon, Wildberries, and Yandex Market
- **Top 5 Results**: Returns the best 5 matching products with details
- **Asynchronous Processing**: Background jobs handle image processing and response generation

## Architecture

### Core Components

1. **TelegramBotMessageHandler**: Handles incoming messages and image uploads
2. **ImageProcessingService**: Creates processing tasks for uploaded images
3. **OpenAIVisionService**: Analyzes images to extract product descriptions
4. **PerplexityVisionService**: Searches for products using Perplexity API
5. **MarketPlaceSearchingService**: Searches products across multiple marketplaces
6. **ResponseGenerationService**: Formats and sends results to users

### Background Jobs

- **ImageAnalyzerJob**: Processes images with hybrid AI approach (OpenAI Vision + Perplexity)
- **ResponseGenerationJob**: Generates and sends responses to users

### Data Flow

1. User sends image → TelegramBotMessageHandler
2. ImageProcessingService creates task → Database
3. ImageAnalyzerJob picks up task → OpenAI Vision analyzes image → Perplexity searches products
4. ResponseGenerationJob formats response → Sends to user

## Configuration

### Required API Keys

```yaml
ai:
  open-ai:
    key: your-openai-api-key
    url: https://api.openai.com
    model: gpt-4o
  perplexity:
    key: your-perplexity-api-key
    url: https://api.perplexity.ai
    model: sonar

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

### Hybrid Approach
The bot uses a two-step AI process:

1. **OpenAI Vision**: Analyzes product images to extract detailed descriptions
2. **Perplexity API**: Searches for matching products based on the description

### OpenAI Vision
- **Model**: `gpt-4o`
- **Capabilities**: Image analysis and description generation
- **Use Case**: Converts product images into detailed text descriptions

### Perplexity API
- **Model**: `sonar`
- **Capabilities**: Real-time web search and product discovery
- **Use Case**: Finds matching products on Russian marketplaces based on descriptions

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
