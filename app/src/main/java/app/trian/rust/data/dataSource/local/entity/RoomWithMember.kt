package app.trian.rust.data.dataSource.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RoomWithMember(
    @Embedded val room: Room,
    @Relation(
        parentColumn = "roomId",
        entityColumn = "roomId"
    )
    val members: List<RoomMember>,
)
