package app.trian.rust.data.dataSource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Room(
    @PrimaryKey
    val roomId: String,
    val roomName: String,
    val roomDescription: String,
    val roomLastContent:String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
