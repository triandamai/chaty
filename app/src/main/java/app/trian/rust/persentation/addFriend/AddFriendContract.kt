package app.trian.rust.persentation.addFriend

import app.trian.rust.model.UserModel

sealed interface AddFriendAction {
    data object SearchUserByUsername : AddFriendAction
    data object SendFriendRequest : AddFriendAction

    data class ChangeShowPopUpSearchUser(val show: Boolean) : AddFriendAction
    data class ChangeShowPopUpFriendRequest(val show: Boolean) : AddFriendAction
    data class ChangeUsernameSearch(val value: String) : AddFriendAction
    data class ChangeSelectedUser(val userModel: UserModel? = null) : AddFriendAction
    data class ChangeMessageRequestFriendship(val message: String) : AddFriendAction
}

sealed interface AddFriendEffect {
    data class ShowToast(val message: String) : AddFriendEffect
}