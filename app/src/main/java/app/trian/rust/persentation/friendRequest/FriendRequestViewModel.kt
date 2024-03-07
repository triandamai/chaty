package app.trian.rust.persentation.friendRequest

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import app.trian.rust.common.BaseViewModel
import app.trian.rust.common.executeAsFlow
import app.trian.rust.data.Response
import app.trian.rust.data.domain.GetListFriendRequestUseCase
import app.trian.rust.data.domain.SendAcceptFriendRequestUseCase
import app.trian.rust.data.domain.SendRejectFriendRequestUseCase
import app.trian.rust.model.FriendRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendRequestViewModel @Inject constructor(
    private val getListFriendRequestUseCase: GetListFriendRequestUseCase,
    private val sendAcceptFriendRequestUseCase: SendAcceptFriendRequestUseCase,
    private val sendRejectFriendRequestUseCase: SendRejectFriendRequestUseCase,
) : BaseViewModel<FriendRequestState, FriendRequestStateImpl, FriendRequestAction, FriendRequestEffect>(
    FriendRequestStateImpl()
) {

    val listStranger: SnapshotStateList<FriendRequestModel> = SnapshotStateList()

    override fun onAction() {
        on(FriendRequestAction.GetListFriendRequest::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow { getListFriendRequestUseCase() }
                    .onStart {
                        state.showLoading = true
                    }
                    .onEach {
                        state.showLoading = false
                        when (it) {
                            is Response.Error -> Unit
                            is Response.Result -> {
                                listStranger.clear()
                                listStranger.addAll(it.data)
                            }
                        }
                    }
                    .collect()
            }
        }
        on(FriendRequestAction.AcceptFriendRequest::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow { sendAcceptFriendRequestUseCase(user.userId) }
                    .onStart {
                        state.showLoading = true
                    }
                    .onEach {
                        state.showLoading = false
                        when (it) {
                            is Response.Error -> {
                                instance send FriendRequestEffect.ShowToast(it.message)
                            }

                            is Response.Result -> {
                                val indexed = listStranger
                                    .withIndex()
                                    .find { value -> value.value.userId == it.data.userId }

                                if (indexed != null) {
                                    listStranger.removeAt(indexed.index)
                                }
                                instance send FriendRequestEffect.ShowToast("Anda sudah berteman")
                            }
                        }
                    }.collect()
            }
        }
        on(FriendRequestAction.RejectFriendRequest::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow { sendRejectFriendRequestUseCase(user.userId) }
                    .onStart {
                        state.showLoading = true
                    }
                    .onEach {
                        state.showLoading = false
                        when (it) {
                            is Response.Error -> Unit
                            is Response.Result -> {
                                val indexed = listStranger
                                    .withIndex()
                                    .find { value -> value.value.userId == it.data }

                                if (indexed != null) {
                                    listStranger.removeAt(indexed.index)
                                }
                            }
                        }
                    }
                    .collect()
            }
        }
    }
}