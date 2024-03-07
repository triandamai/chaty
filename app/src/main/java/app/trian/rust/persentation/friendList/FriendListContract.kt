package app.trian.rust.persentation.friendList

import app.trian.rust.model.UserModel

sealed interface FriendListAction {
    data object GetListFriend : FriendListAction

    data class OnFriendAdded(val friend:UserModel):FriendListAction
    data class OnFriendRemove(val friend:UserModel):FriendListAction
    data class OnFriendChanged(val friend:UserModel):FriendListAction
}

sealed interface FriendListEffect {

}