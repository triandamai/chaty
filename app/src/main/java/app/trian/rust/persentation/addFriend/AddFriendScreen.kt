package app.trian.rust.persentation.addFriend

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.rust.common.Either
import app.trian.rust.common.On
import app.trian.rust.component.BottomSheetSendFriendRequest
import app.trian.rust.component.CardUser
import app.trian.rust.component.CustomAppbar
import app.trian.rust.component.PopUpLoading
import app.trian.rust.component.PopUpRequestFriend
import app.trian.rust.component.PopUpSearchUser
import app.trian.rust.model.UserModel
import app.trian.rust.ui.theme.AndroidRustTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    modifier: Modifier = Modifier,
    nav: NavHostController,
    listUser: SnapshotStateList<UserModel>,
    stateFlow: Flow<AddFriendState>,
    effectFlow: Flow<Either<AddFriendEffect>>,
    action: (AddFriendAction) -> Unit,
) {
    val ctx = LocalContext.current
    val state by stateFlow.collectAsState(initial = AddFriendState())
    val effect by effectFlow.collectAsState(initial = Either.left())

    val scope = rememberCoroutineScope()


    On(type = AddFriendEffect.ShowToast::class.java, effect = effect) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }

    PopUpLoading(show = state.showLoading)
    PopUpSearchUser(
        show = state.showPopUpSearchUser,
        value = state.username.orEmpty(),
        onChange = { action(AddFriendAction.ChangeUsernameSearch(it)) },
        onDismiss = {
            action(AddFriendAction.ChangeShowPopUpSearchUser(false))
        },
        onSend = {
            action(AddFriendAction.SearchUserByUsername)
        }
    )
    PopUpRequestFriend(
        show = state.showPopUpRequestFriend,
        fullName = state.selectedUser?.fullName.orEmpty(),
        profilePicture = state.selectedUser?.profilePicture.orEmpty(),
        message = state.message,
        onMessageChange = {
            action(AddFriendAction.ChangeMessageRequestFriendship(it))
        },
        onDismiss = {
            action(AddFriendAction.ChangeShowPopUpFriendRequest(false))
        },
        onSend = {
            action(AddFriendAction.ChangeShowPopUpFriendRequest(false))
            action(AddFriendAction.SendFriendRequest)
        }
    )


    Scaffold(
        topBar = {
            CustomAppbar(
                title = "Add Friend",
                subtitle = "",
                actions = {
                    IconButton(
                        enabled = true,
                        onClick = {
                            action(AddFriendAction.ChangeShowPopUpSearchUser(true))
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = ""
                            )
                        }
                    )
                    IconButton(
                        onClick = {
                            nav.navigate("friend-request") {
                                launchSingleTop = true
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.PeopleOutline,
                                contentDescription = ""
                            )
                        }
                    )

                },
                navIcon = {},
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.padding(it),
            content = {
                if (listUser.isEmpty()) {
                    item {
                        Column(
                            modifier = modifier.fillParentMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (state.username.isNullOrEmpty()) {
                                Text(text = "Cari user dengan menekan tombol  '\uD83D\uDD0D'")
                            } else {
                                Text(text = "User @${state.username} tidak ditemukan")
                            }
                        }
                    }
                }
                items(listUser, key = { user -> user.userId }) { user ->
                    Row(
                        modifier = modifier
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        CardUser(
                            name = user.fullName,
                            bio = user.bio,
                            lastOnline = "6 menit yang lalu",
                            profilePicture = user.profilePicture,
                            onAddClick = {
                                action(AddFriendAction.ChangeSelectedUser(user))
                                action(AddFriendAction.ChangeShowPopUpFriendRequest(true))
                            },
                            onMoreOptionClick = {}
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewAddFriendScreen() {
    AndroidRustTheme {
        AddFriendScreen(
            nav = rememberNavController(),
            listUser = SnapshotStateList(),
            stateFlow = flowOf(),
            effectFlow = flowOf(),
            action = {}
        )
    }
}