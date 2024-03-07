package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.data.repositories.ChatRepository
import app.trian.rust.data.repositories.RoomRepository
import app.trian.rust.model.ChatModel
import app.trian.rust.model.RoomModel
import javax.inject.Inject

class DeleteChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(chat: ChatModel): Response<Int> {
        val data = chatRepository.deleteRoomFromCloud(chat)
        return Response.success(data)
    }
}