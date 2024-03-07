package app.trian.rust.persentation.friendList

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import app.trian.rust.common.BaseViewModel
import app.trian.rust.common.executeAsFlow
import app.trian.rust.data.Response
import app.trian.rust.data.domain.GetListFriendRequestUseCase
import app.trian.rust.data.domain.GetListFriendUseCase
import app.trian.rust.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendListViewModel @Inject constructor(
    private val getListFriendUseCase: GetListFriendUseCase,
) : BaseViewModel<FriendListState, FriendListStateImpl, FriendListAction, FriendListEffect>(
    FriendListStateImpl()
) {

    val listFriend: SnapshotStateList<UserModel> = SnapshotStateList()

    override fun onAction() {
        on(FriendListAction.GetListFriend::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow { getListFriendUseCase() }
                    .onStart {
                        state.showLoading = true
                    }
                    .onEach {
                        state.showLoading = false
                        when (it) {
                            is Response.Error -> Unit
                            is Response.Result -> {
                                listFriend.clear()
                                listFriend.addAll(it.data)
                            }
                        }
                    }.collect()
            }
        }

        on(FriendListAction.OnFriendAdded::class.java) { state ->
            viewModelScope.launch {
                val findIndex = listFriend.withIndex()
                    .find { indexed -> indexed.value.userId == friend.userId }
                if (findIndex != null) {
                    listFriend[findIndex.index] = friend
                } else {
                    listFriend.add(friend)
                }
            }
        }
        on(FriendListAction.OnFriendChanged::class.java) { state ->
            viewModelScope.launch {
                val findIndex = listFriend.withIndex()
                    .find { indexed -> indexed.value.userId == friend.userId }
                if (findIndex != null) {
                    listFriend[findIndex.index] = friend
                } else {
                    listFriend.add(friend)
                }
            }
        }
        on(FriendListAction.OnFriendRemove::class.java) { state ->
            viewModelScope.launch {
                val findIndex = listFriend.withIndex()
                    .find { indexed -> indexed.value.userId == friend.userId }
                if (findIndex != null) {
                    listFriend.removeAt(findIndex.index)
                }
            }
        }
    }
}