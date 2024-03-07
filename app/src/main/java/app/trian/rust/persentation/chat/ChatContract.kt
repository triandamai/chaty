package app.trian.rust.persentation.chat

import app.trian.rust.data.dataSource.local.entity.Chat
import app.trian.rust.model.ChatModel

sealed interface ChatAction {
    data object None : ChatAction

    data class SubmitChat(val content: String) : ChatAction

    data object GetChatByRoomId : ChatAction
    data object GetDetailRoom : ChatAction

    data class OnChatAdded(val chat: ChatModel) : ChatAction
    data class OnChatRemove(val chat: ChatModel) : ChatAction
    data class OnChatChanged(val chat: ChatModel) : ChatAction
}

sealed interface ChatEffect {
    data object None : ChatEffect


}