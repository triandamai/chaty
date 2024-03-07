package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.data.repositories.RoomRepository
import app.trian.rust.model.RoomModel
import javax.inject.Inject

class DeleteRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(room: RoomModel): Response<Int> {
        val data = roomRepository.deleteRoomFromCloud(room)
        return Response.success(data)
    }
}