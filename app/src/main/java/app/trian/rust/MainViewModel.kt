package app.trian.rust

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.trian.rust.common.executeAsFlow
import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.Chat
import app.trian.rust.data.dataSource.local.entity.Friend
import app.trian.rust.data.dataSource.local.entity.Room
import app.trian.rust.data.domain.DeleteChatUseCase
import app.trian.rust.data.domain.DeleteFriendUseCase
import app.trian.rust.data.domain.DeleteRoomUseCase
import app.trian.rust.data.domain.InsertChatUseCase
import app.trian.rust.data.domain.InsertFriendUseCase
import app.trian.rust.data.domain.InsertRoomUseCase
import app.trian.rust.model.ChatModel
import app.trian.rust.model.RoomModel
import app.trian.rust.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val insertRoomUseCase: InsertRoomUseCase,
    private val insertChatUseCase: InsertChatUseCase,
    private val insertFriendUseCase: InsertFriendUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val deleteFriendUseCase: DeleteFriendUseCase,
) : ViewModel() {

    fun insertRoom(room: RoomModel, member: List<UserModel>, callback: (Room) -> Unit) =
        viewModelScope.launch {
            executeAsFlow { insertRoomUseCase(room, member) }
                .onStart { }
                .onEach {
                    if (it is Response.Result) {
                        callback(it.data)
                    }
                }
                .collect()
        }

    fun deleteRoom(room: RoomModel, callback: (Int) -> Unit) = viewModelScope.launch {
        executeAsFlow { deleteRoomUseCase(room) }
            .onStart { }
            .onEach {
                if (it is Response.Result) {
                    callback(it.data)
                }
            }
            .collect()
    }

    fun deleteChat(chat: ChatModel, callback: (Int) -> Unit) = viewModelScope.launch {
        executeAsFlow { deleteChatUseCase(chat) }
            .onStart { }
            .onEach {
                if (it is Response.Result) {
                    callback(it.data)
                }
            }
            .collect()
    }

    fun insertChat(chat: ChatModel, callback: (Chat) -> Unit) = viewModelScope.launch {
        executeAsFlow { insertChatUseCase(chat) }
            .onStart { }
            .onEach {
                if (it is Response.Result) {
                    callback(it.data)
                }
            }
            .collect()
    }

    fun insertFriend(userModel: UserModel, callback: (Friend) -> Unit) = viewModelScope.launch {
        executeAsFlow { insertFriendUseCase(userModel) }
            .onStart { }
            .onEach {
                if (it is Response.Result) {
                    callback(it.data)
                }
            }
            .collect()
    }

    fun deleteFriend(userModel: UserModel, callback: (Int) -> Unit) = viewModelScope.launch {
        executeAsFlow { deleteFriendUseCase(userModel) }
            .onStart { }
            .onEach {
                if (it is Response.Result) {
                    callback(it.data)
                }
            }
            .collect()
    }
}