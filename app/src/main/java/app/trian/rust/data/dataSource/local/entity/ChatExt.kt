package app.trian.rust.data.dataSource.local.entity

import java.time.LocalDateTime
import java.util.UUID

fun CreateChatEntity(content: String, author: String, room: String): Chat {
    val currentDate = LocalDateTime.now()

    val chat = Chat(
        roomId = room,
        chatId = UUID.randomUUID().toString(),
        authorId = author,
        content = content,
        createdAt = currentDate,
        updatedAt = currentDate
    )

    return chat
}