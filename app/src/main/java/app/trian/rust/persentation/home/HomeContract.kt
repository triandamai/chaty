package app.trian.rust.persentation.home

import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.model.RoomModel

sealed interface HomeAction{
    data object None:HomeAction
    data object GetListRoom:HomeAction

    data class OnRoomAdded(val room: RoomModel):HomeAction
    data class OnRoomChanged(val room: RoomModel):HomeAction
    data class OnRoomRemove(val room: RoomModel):HomeAction

}

sealed interface HomeEffect{
    data object None:HomeEffect
}