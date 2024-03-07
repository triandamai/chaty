package app.trian.rust.persentation.addFriend

import app.trian.generator.State
import app.trian.rust.model.FriendRequestModel
import app.trian.rust.model.UserModel
import javax.annotation.concurrent.Immutable

@State
@Immutable
data class AddFriendState(
    val showLoading: Boolean = false,
    val showPopUpSearchUser: Boolean = false,
    val showPopUpRequestFriend: Boolean = false,

    val username: String? = null,
    val selectedUser: FriendRequestModel? = null,

    val message: String = "Hi, Mari kita berteman.",
)
