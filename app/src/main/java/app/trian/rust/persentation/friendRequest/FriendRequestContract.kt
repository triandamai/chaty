package app.trian.rust.persentation.friendRequest

import app.trian.rust.model.FriendRequestModel

sealed interface FriendRequestAction {
    data object GetListFriendRequest : FriendRequestAction

    data class AcceptFriendRequest(val user: FriendRequestModel) : FriendRequestAction
    data class RejectFriendRequest(val user: FriendRequestModel) : FriendRequestAction
}

sealed interface FriendRequestEffect {
    data class ShowToast(val message:String):FriendRequestEffect
}