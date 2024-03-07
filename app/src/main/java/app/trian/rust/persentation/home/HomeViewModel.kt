package app.trian.rust.persentation.home

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import app.trian.rust.common.BaseViewModel
import app.trian.rust.common.executeAsFlow
import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.RoomWithMember
import app.trian.rust.data.domain.GetListRoomWithMemberUseCase
import app.trian.rust.data.domain.GetRoomWithMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getListRoomWithMemberUseCase: GetListRoomWithMemberUseCase,
    private val getRoomWithMemberUseCase: GetRoomWithMemberUseCase,
) : BaseViewModel<HomeState, HomeStateImpl, HomeAction, HomeEffect>(
    HomeStateImpl()
) {
    val listRoom: SnapshotStateList<RoomWithMember> = SnapshotStateList()

    override fun onAction() {
        on(HomeAction.GetListRoom::class.java) {
            viewModelScope.launch {
                val data = getListRoomWithMemberUseCase()
                listRoom.clear()
                listRoom.addAll(data)
            }
        }

        on(HomeAction.OnRoomAdded::class.java) {
            viewModelScope.launch {
                executeAsFlow { getRoomWithMemberUseCase(room.roomId) }
                    .onStart { }
                    .onEach {
                        if (it is Response.Result) {
                            val findIndex =
                                listRoom.withIndex()
                                    .find { room -> room.value.room.roomId == this@on.room.roomId }
                            if (findIndex != null) {
                                listRoom[findIndex.index] = it.data
                            } else {
                                listRoom.add(0, it.data)
                            }
                        }
                    }
                    .collect()
            }
        }
        on(HomeAction.OnRoomRemove::class.java) {
            val findIndex =
                listRoom.withIndex().find { room -> room.value.room.roomId == this.room.roomId }
            if (findIndex != null) {
                listRoom.removeAt(findIndex.index)
            }
        }
        on(HomeAction.OnRoomChanged::class.java) {
            viewModelScope.launch {
                executeAsFlow { getRoomWithMemberUseCase(room.roomId) }
                    .onStart { }
                    .onEach {
                        if (it is Response.Result) {
                            val findIndex =
                                listRoom.withIndex()
                                    .find { room -> room.value.room.roomId == this@on.room.roomId }
                            if (findIndex != null) {
                                listRoom[findIndex.index] = it.data
                            } else {
                                listRoom.add(0, it.data)
                            }
                        }

                    }
                    .collect()
            }
        }
    }
}