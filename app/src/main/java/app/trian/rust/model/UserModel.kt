package app.trian.rust.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("userId")
    val userId: String = "",
    @SerializedName("username")
    val username: String="",
    @SerializedName("fullName")
    val fullName: String = "",
    @SerializedName("profilePicture")
    val profilePicture: String = "",
    @SerializedName("bio")
    val bio: String = "",
    @SerializedName("banner")
    val banner: String = "",
    @SerializedName("createdAt")
    val createdAt: Long = 0,
    @SerializedName("updatedAt")
    val updatedAt: Long = 0,
)
//KDBbmtvBvzPYGCMi25mBH7ytHSi2
