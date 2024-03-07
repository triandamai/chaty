package app.trian.rust.data.dataSource.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ChatWithDetail(
    @Embedded val chat: Chat,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId"
    )
    val chatAttachment: List<ChatAttachment>,
)
