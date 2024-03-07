package app.trian.rust.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.data.dataSource.local.entity.RoomWithMember

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoom(vararg room: Room)

    @Query("DELETE FROM Room WHERE roomId=:roomId")
    fun deleteRoom(roomId:String):Int

    @Transaction
    @Query("SELECT * FROM Room ORDER BY updatedAt DESC")
    fun getListRoom():List<Room>

    @Transaction
    @Query("SELECT * FROM Room WHERE roomId=:roomId")
    fun getDetailRoomWithMember(roomId: String): RoomWithMember?

    @Transaction
    @Query("SELECT * FROM Room ORDER BY updatedAt DESC")
    fun getListRoomWithMember(): List<RoomWithMember>

}