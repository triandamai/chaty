@file:OptIn(ExperimentalMaterial3Api::class)

package app.trian.rust.persentation.friendList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.rust.common.Either
import app.trian.rust.component.CardFriend
import app.trian.rust.component.CustomAppbar
import app.trian.rust.model.UserModel
import app.trian.rust.persentation.addFriend.AddFriendAction
import app.trian.rust.ui.theme.AndroidRustTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun FriendListScreen(
    modifier: Modifier = Modifier,
    nav: NavHostController,
    listFriend: SnapshotStateList<UserModel> = SnapshotStateList(),
    stateFlow: Flow<FriendListState> = flowOf(),
    effectFlow: Flow<Either<FriendListEffect>> = flowOf(),
    action: (FriendListAction) -> Unit,
) {
    val state by stateFlow.collectAsState(initial = FriendListState())
    val effect by effectFlow.collectAsState(initial = Either.left())

    LaunchedEffect(key1 = Unit) {
        action(FriendListAction.GetListFriend)
    }
    Scaffold(
        topBar = {
            CustomAppbar(
                title = "Daftar pertemanana",
                subtitle = "",
                actions = {
                    IconButton(
                        onClick = {

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
                            nav.navigate("add-friend") {
                                launchSingleTop = true
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Outlined.GroupAdd,
                                contentDescription = ""
                            )
                        }
                    )

                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.padding(it)
        ) {
            if (listFriend.isEmpty()) {
                item {
                    Column(
                        modifier = modifier.fillParentMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Kamu belum memiliki teman")
                        Text(
                            text = buildAnnotatedString {
                                append("Yuk cari teman dengan menekan tombol +")
                            }
                        )
                    }
                }
            }

            items(listFriend, key = { friend -> friend.userId }) { friend ->
                CardFriend(
                    name = "@".plus(friend.username.plus("(").plus(friend.fullName).plus(")")),
                    bio = friend.bio,
                    lastOnline = "2 Menit lalu",
                    onMoreOptionClick = {},
                    onSendMessage = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewFriendListScreen() {
    AndroidRustTheme {
        FriendListScreen(nav = rememberNavController()) {

        }
    }
}