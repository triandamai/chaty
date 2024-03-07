package app.trian.rust.model

import com.google.gson.annotations.SerializedName

data class RoomModel(
    @SerializedName("roomId")
    val roomId: String = "",
    @SerializedName("roomName")
    val roomName: String = "",
    @SerializedName("roomDescription")
    val roomDescription: String = "",
    @SerializedName("roomLastContent")
    val roomLastContent: String = "",
    @SerializedName("members")
    val members: List<String> = listOf(),
    @SerializedName("createdAt")
    val createdAt: Long = 0,
    @SerializedName("updatedAt")
    val updatedAt: Long = 0,
)
//KDBbmtvBvzPYGCMi25mBH7ytHSi2
