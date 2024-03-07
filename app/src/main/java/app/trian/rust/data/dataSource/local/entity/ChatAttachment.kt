package app.trian.rust.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class ChatAttachment(
    @PrimaryKey
    val attachmentId: String,
    val chatId: String,
    val attachmentType: AttachmentType,
    val url: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
