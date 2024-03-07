package app.trian.rust.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.trian.rust.data.dataSource.local.entity.Friend

@Dao
interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFriend(vararg friend: Friend): Int

    @Query("DELETE FROM friend WHERE userId=:userId")
    fun deleteFriend(userId: String): Int
}