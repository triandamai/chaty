package app.trian.rust.data.domain

import app.trian.rust.data.dataSource.local.entity.ChatWithDetail
import app.trian.rust.data.dataSource.local.entity.RoomWithMember
import app.trian.rust.data.repositories.ChatRepository
import javax.inject.Inject

class GetListChatByRoomUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(roomId:String): List<ChatWithDetail> {
        return chatRepository.getListChatByRoom(roomId)
    }
}
