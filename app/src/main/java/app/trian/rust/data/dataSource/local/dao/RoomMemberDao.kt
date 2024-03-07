package app.trian.rust.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import app.trian.rust.data.dataSource.local.entity.RoomMember

@Dao
interface RoomMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoomMember(vararg roomMember: RoomMember)
}