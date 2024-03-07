package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.RoomWithMember
import app.trian.rust.data.repositories.ChatRepository
import app.trian.rust.data.repositories.RoomRepository
import javax.inject.Inject

class GetRoomWithMemberUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(roomId: String): Response<RoomWithMember> {
        return roomRepository.getRoomWithMember(roomId)
    }
}
