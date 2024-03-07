package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.data.repositories.RoomRepository
import app.trian.rust.model.RoomModel
import app.trian.rust.model.UserModel
import javax.inject.Inject

class InsertRoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
) {
    suspend operator fun invoke(room: RoomModel,member:List<UserModel>): Response<Room> {
        val data = roomRepository.insertRoomFromCloud(room,member)
        return Response.success(data)
    }
}