package app.trian.rust.persentation.chat

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.trian.rust.common.BaseViewModel
import app.trian.rust.common.executeAsFlow
import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.entity.ChatAttachment
import app.trian.rust.data.dataSource.local.entity.ChatWithDetail
import app.trian.rust.data.domain.GetChatUseCase
import app.trian.rust.data.domain.GetListChatByRoomUseCase
import app.trian.rust.data.domain.GetRoomWithMemberUseCase
import app.trian.rust.data.domain.SendChatByRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getListChatByRoomUseCase: GetListChatByRoomUseCase,
    private val getChatUseCase: GetChatUseCase,
    private val getRoomWithMemberUseCase: GetRoomWithMemberUseCase,
    private val sendChatByRoomUseCase: SendChatByRoomUseCase,
) : BaseViewModel<ChatState, ChatStateImpl, ChatAction, ChatEffect>(
    ChatStateImpl()
) {
    val listChat: SnapshotStateList<ChatWithDetail> = SnapshotStateList()
    private fun getRoomId(): String = savedStateHandle.get<String>("roomId").orEmpty()


    override fun onAction() {
        on(ChatAction.GetDetailRoom::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow { getRoomWithMemberUseCase(getRoomId()) }
                    .onStart { }
                    .onEach {
                        when (it) {
                            is Response.Error -> Unit
                            is Response.Result -> {
                                state.currentRoom = it.data
                            }
                        }
                    }
                    .collect()
            }
        }
        on(ChatAction.GetChatByRoomId::class.java) {
            viewModelScope.launch {
                val data = getListChatByRoomUseCase(getRoomId())
                listChat.addAll(data)
            }
        }

        on(ChatAction.SubmitChat::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow {
                    sendChatByRoomUseCase(
                        roomId = getRoomId(),
                        content = content,
                        sendTo = listOf("tesasaja"),
                        attachments = listOf()
                    )
                }.onStart {

                }.onEach {
                    when (it) {
                        is Response.Error -> Unit
                        is Response.Result -> {
                            listChat.add(0, it.data)
                        }
                    }
                }.collect()
            }
        }
        on(ChatAction.OnChatAdded::class.java) {
            viewModelScope.launch {
                val chat = this@on.chat
                executeAsFlow { getChatUseCase(chat.chatId) }
                    .onStart { }
                    .onEach {
                        if (it is Response.Result) {
                            val findIndex = listChat.withIndex()
                                .find { find -> find.value.chat.chatId == chat.chatId }
                            if (findIndex == null) {
                                listChat.add(0, it.data)
                            } else {
                                listChat[findIndex.index] = it.data
                            }
                        }
                    }
                    .collect()
            }
        }
        on(ChatAction.OnChatChanged::class.java) {
            viewModelScope.launch {
                executeAsFlow { getChatUseCase(this@on.chat.chatId) }
                    .onStart { }
                    .onEach {
                        if (it is Response.Result) {
                            val findIndex = listChat.withIndex()
                                .find { find -> find.value.chat.chatId == chat.chatId }
                            if (findIndex == null) {
                                listChat.add(0, it.data)
                            } else {
                                listChat[findIndex.index] = it.data
                            }
                        }
                    }
                    .collect()
            }
        }
        on(ChatAction.OnChatRemove::class.java) {
            viewModelScope.launch {
                val chat = this@on.chat
                val findIndex = listChat.withIndex()
                    .find { find -> find.value.chat.chatId == chat.chatId }
                if (findIndex != null) {
                    listChat.removeAt(findIndex.index)
                }
            }
        }
    }
}