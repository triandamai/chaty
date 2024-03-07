package app.trian.rust.persentation.chat

import app.trian.generator.State
import app.trian.rust.data.dataSource.local.entity.RoomWithMember
import javax.annotation.concurrent.Immutable

@State
@Immutable
data class ChatState(
    val currentUser: String = "",
    val currentRoom: RoomWithMember? = null,
)