package app.trian.rust.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Friend(
    @PrimaryKey
    val userId: String = "",
    val username: String = "",
    val fullName: String = "",
    val profilePicture: String = "",
    val bio: String = "",
    val banner: String = "",
    val isFriend: Boolean = true,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
