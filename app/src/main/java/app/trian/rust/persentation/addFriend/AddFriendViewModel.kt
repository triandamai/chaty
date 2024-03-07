package app.trian.rust.persentation.addFriend

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import app.trian.rust.common.BaseViewModel
import app.trian.rust.common.executeAsFlow
import app.trian.rust.data.Response
import app.trian.rust.data.domain.SearchUserUseCase
import app.trian.rust.data.domain.SendFriendRequestUseCase
import app.trian.rust.model.FriendRequestModel
import app.trian.rust.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddFriendViewModel @Inject constructor(
    private val searchUserUseCase: SearchUserUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
) : BaseViewModel<AddFriendState, AddFriendStateImpl, AddFriendAction, AddFriendEffect>(
    AddFriendStateImpl()
) {

    val listUser: SnapshotStateList<UserModel> = SnapshotStateList()

    override fun onAction() {
        on(AddFriendAction.ChangeUsernameSearch::class.java) { state ->
            state.username = value
        }
        on(AddFriendAction.ChangeMessageRequestFriendship::class.java) { state ->
            state.message = message
        }
        on(AddFriendAction.ChangeShowPopUpSearchUser::class.java) { state ->
            state.showPopUpSearchUser = show
        }
        on(AddFriendAction.ChangeShowPopUpFriendRequest::class.java){ state->
            state.showPopUpRequestFriend = show
        }
        on(AddFriendAction.ChangeSelectedUser::class.java) { state ->
            if (userModel != null) {
                state.selectedUser = FriendRequestModel(
                    userId = userModel.userId,
                    fullName = userModel.fullName,
                    profilePicture = userModel.profilePicture,
                    bio = userModel.profilePicture,
                    message = state.message,
                    createdAt = userModel.createdAt,
                    updatedAt = userModel.updatedAt
                )
            } else {
                state.selectedUser = null
            }
        }
        on(AddFriendAction.SearchUserByUsername::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow { searchUserUseCase(state.username.orEmpty()) }
                    .onStart {
                        state.showLoading = true
                    }
                    .onEach {
                        state.showLoading = false
                        when (it) {
                            is Response.Error -> Unit
                            is Response.Result -> {
                                state.showPopUpSearchUser = false
                                state.username = null
                                listUser.clear()
                                listUser.addAll(it.data)
                            }
                        }
                    }
                    .collect()
            }
        }

        on(AddFriendAction.SendFriendRequest::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow { sendFriendRequestUseCase(state.selectedUser) }
                    .onStart {
                        state.showLoading = true
                    }
                    .onEach {
                        state.showLoading = false
                        when (it) {
                            is Response.Error -> Unit
                            is Response.Result -> {
                                instance send AddFriendEffect.ShowToast("Permintaan pertemanan dikirim...")
                                state.selectedUser = null
                                state.message = ""
                            }
                        }
                    }
                    .collect()
            }
        }
    }
}