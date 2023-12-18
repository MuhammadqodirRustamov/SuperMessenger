package uni.dev.supermessenger.model

data class Message(
    val to: String? = null,
    val from: String? = null,
    val text: String? = null,
    val date: String? = null,
    val key: String? = null
)