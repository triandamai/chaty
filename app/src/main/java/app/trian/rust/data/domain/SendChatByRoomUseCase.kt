package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.ChatAttachment
import app.trian.rust.data.dataSource.local.entity.ChatWithDetail
import app.trian.rust.data.repositories.ChatRepository
import app.trian.rust.data.repositories.UserRepository
import javax.inject.Inject

class SendChatByRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(
        roomId: String,
        content: String,
        sendTo: List<String>,
        attachments: List<ChatAttachment>,
    ): Response<ChatWithDetail> {

        return chatRepository.sendChatByRoom(
            roomId = roomId,
            content = content,
            sendTo = sendTo
        )
    }
}
