package app.trian.rust.data.repositories

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.MainDatabase
import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.data.dataSource.local.entity.RoomMember
import app.trian.rust.data.dataSource.local.entity.RoomWithMember
import app.trian.rust.model.RoomModel
import app.trian.rust.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class RoomRepository @Inject constructor(
    private val db: MainDatabase,
) {
    companion object{
        const val ROOM = "ROOM"
        const val FIELD_MEMBER = "members"
    }
    suspend fun getListRoomWithMember(): List<RoomWithMember> = withContext(Dispatchers.IO) {
        db.roomDao().getListRoomWithMember()
    }

    suspend fun getRoomWithMember(roomId: String): Response<RoomWithMember> =
        withContext(Dispatchers.IO) {
            val data = db.roomDao().getDetailRoomWithMember(roomId)
            if (data != null) {
                Response.success(data)
            } else {
                Response.error()
            }
        }

    suspend fun insertRoomFromCloud(roomModel: RoomModel, member: List<UserModel>): Room =
        withContext(Dispatchers.IO) {
            val room = Room(
                roomId = roomModel.roomId,
                roomName = roomModel.roomName,
                roomDescription = roomModel.roomDescription,
                roomLastContent = roomModel.roomLastContent,
                createdAt = LocalDateTime.ofEpochSecond(roomModel.createdAt, 0, ZoneOffset.UTC),
                updatedAt = LocalDateTime.ofEpochSecond(roomModel.updatedAt, 0, ZoneOffset.UTC)
            )
            val roomMember = member.map { user ->
                RoomMember(
                    userId = user.userId,
                    roomId = room.roomId,
                    fullName = user.fullName,
                    username = user.username,
                    profilePicture = user.profilePicture,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            }
            db.roomMemberDao().insertRoomMember(*roomMember.toTypedArray())
            db.roomDao().insertRoom(room)
            room
        }

    suspend fun deleteRoomFromCloud(roomModel: RoomModel): Int = withContext(Dispatchers.IO) {
        db.roomDao().deleteRoom(roomModel.roomId)
    }


}