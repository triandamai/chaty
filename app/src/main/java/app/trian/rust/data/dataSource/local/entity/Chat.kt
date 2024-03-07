package app.trian.rust.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Chat(
    @PrimaryKey
    val chatId: String,
    val roomId: String,
    val authorId:String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)