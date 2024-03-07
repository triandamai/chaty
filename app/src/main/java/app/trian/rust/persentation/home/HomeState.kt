package app.trian.rust.persentation.home

import androidx.compose.runtime.Immutable
import app.trian.generator.State
import app.trian.rust.common.ImplClass
import app.trian.rust.data.dataSource.local.entity.RoomWithMember

@State
@Immutable
data class HomeState(
    val listRoom: List<RoomWithMember> = listOf(),
)