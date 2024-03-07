package app.trian.rust.data.dataSource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import app.trian.rust.data.dataSource.local.entity.ChatAttachment

@Dao
interface AttachmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAttachment(vararg attachment: ChatAttachment)
}