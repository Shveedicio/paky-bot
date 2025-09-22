import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PerplexityResponse(
  val id: String,
  val model: String,
  val created: Long,
//  val usage: Usage,
  val citations: List<String>,
  @JsonProperty("search_results")
  val searchResults: List<SearchResult>,
  @JsonProperty("object")
  val objectType: String,
  val choices: List<Choice>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Usage(
  @JsonProperty("prompt_tokens")
  val promptTokens: Int,
  @JsonProperty("completion_tokens")
  val completionTokens: Int,
  @JsonProperty("total_tokens")
  val totalTokens: Int,
  @JsonProperty("search_context_size")
  val searchContextSize: String,
//  val cost: Cost,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Cost(
  @JsonProperty("input_tokens_cost")
  val inputTokensCost: Double,
  @JsonProperty("output_tokens_cost")
  val outputTokensCost: Double,
  @JsonProperty("request_cost")
  val requestCost: Double,
  @JsonProperty("total_cost")
  val totalCost: Double,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SearchResult(
  val title: String,
  val url: String,
  val date: String? = null,
  val snippet: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Choice(
  val index: Int,
  @JsonProperty("finish_reason")
  val finishReason: String,
  val message: Message,
  val delta: Delta,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(val role: String, val content: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Delta(val role: String, val content: String)
