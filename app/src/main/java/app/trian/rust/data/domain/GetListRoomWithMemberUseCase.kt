package app.trian.rust.data.domain

import app.trian.rust.data.dataSource.local.entity.RoomWithMember
import app.trian.rust.data.repositories.ChatRepository
import app.trian.rust.data.repositories.RoomRepository
import javax.inject.Inject

class GetListRoomWithMemberUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(): List<RoomWithMember> {
        return roomRepository.getListRoomWithMember()
    }
}
