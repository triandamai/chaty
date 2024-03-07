package app.trian.rust.persentation.friendRequest

import app.trian.generator.State
import javax.annotation.concurrent.Immutable

@State
@Immutable
data class FriendRequestState(
    val showLoading: Boolean = false,

)
