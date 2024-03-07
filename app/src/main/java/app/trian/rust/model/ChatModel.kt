package app.trian.rust.model

import com.google.gson.annotations.SerializedName

data class ChatModel(
    @SerializedName("chatId")
    val chatId: String="",
    @SerializedName("roomId")
    val roomId: String="",
    @SerializedName("authorId")
    val authorId: String="",
    @SerializedName("content")
    val content: String="",
    @SerializedName("createdAt")
    val createdAt: Long=0,
    @SerializedName("updatedAt")
    val updatedAt: Long=0,
)
