package app.trian.rust.persentation.friendList

import app.trian.generator.State
import javax.annotation.concurrent.Immutable

@State
@Immutable
data class FriendListState(
    val showLoading: Boolean = false,
)
