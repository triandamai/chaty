package app.trian.rust.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.trian.rust.data.dataSource.local.entity.Chat
import app.trian.rust.data.dataSource.local.entity.ChatWithDetail

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(vararg chat: Chat)

    @Query("DELETE FROM chat WHERE chatId=:chatId")
    fun deleteChat(chatId: String): Int

    @Transaction
    @Query("SELECT * FROM Chat WHERE roomId= :roomId ORDER BY updatedAt DESC")
    fun getChatsByRoom(roomId: String): List<ChatWithDetail>

    @Transaction
    @Query("SELECT * FROM Chat WHERE chatId= :chatId")
    fun getChatById(chatId: String): ChatWithDetail?
}