package app.trian.rust.data.dataSource.local

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import app.trian.rust.data.dataSource.local.dao.AttachmentDao
import app.trian.rust.data.dataSource.local.dao.ChatDao
import app.trian.rust.data.dataSource.local.dao.FriendDao
import app.trian.rust.data.dataSource.local.dao.RoomDao
import app.trian.rust.data.dataSource.local.dao.RoomMemberDao
import app.trian.rust.data.dataSource.local.entity.Chat
import app.trian.rust.data.dataSource.local.entity.ChatAttachment
import app.trian.rust.data.dataSource.local.entity.Friend
import app.trian.rust.data.dataSource.local.entity.RoomMember

@Database(
    entities = [
        Chat::class,
        ChatAttachment::class,
        app.trian.rust.data.dataSource.local.entity.Room::class,
        RoomMember::class,
        Friend::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(
    value = [
        DateTimeConverter::class
    ]
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun roomDao(): RoomDao
    abstract fun chatAttachmentDao(): AttachmentDao
    abstract fun roomMemberDao(): RoomMemberDao
    abstract fun friendDao():FriendDao

    companion object {
        private const val DB_NAME: String = "trianapp"
        private var INSTANCE: MainDatabase? = null

        fun getInstance(
            ctx: Context,
        ): MainDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    ctx.applicationContext,
                    MainDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            try {
//                                db.beginTransaction()
                                val values = ContentValues()

                                values.put("id", "id_app_setting")

//                                db.insert("AppSetting", SQLiteDatabase.CONFLICT_REPLACE, values)
//                                db.setTransactionSuccessful()
//                                db.endTransaction()
                            } catch (e: Exception) {
//                                e.printStackTrace()
//                                db.endTransaction()
                            }
                        }
                    })
                    .build()
            }
            return INSTANCE!!
        }
    }

}