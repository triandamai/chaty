@file:OptIn(ExperimentalMaterial3Api::class)

package app.trian.rust.persentation.friendRequest

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.rust.common.Either
import app.trian.rust.common.On
import app.trian.rust.component.CardFriendRequest
import app.trian.rust.component.CustomAppbar
import app.trian.rust.component.PopUpLoading
import app.trian.rust.model.FriendRequestModel
import app.trian.rust.ui.theme.AndroidRustTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun FriendRequestScreen(
    modifier: Modifier = Modifier,
    nav: NavHostController,
    stateFlow: Flow<FriendRequestState> = flowOf(),
    effectFlow: Flow<Either<FriendRequestEffect>> = flowOf(),
    listStranger: SnapshotStateList<FriendRequestModel> = SnapshotStateList(),
    action: (FriendRequestAction) -> Unit = {},
) {
    val ctx = LocalContext.current
    val state by stateFlow.collectAsState(initial = FriendRequestState())
    val effect by effectFlow.collectAsState(initial = Either.left())

    LaunchedEffect(key1 = Unit) {
        action(FriendRequestAction.GetListFriendRequest)
    }
    On(type = FriendRequestEffect.ShowToast::class.java, effect = effect) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }
    PopUpLoading(show = state.showLoading)
    Scaffold(
        topBar = {
            CustomAppbar(
                title = "Permintaan pertemanan",
                subtitle = "",
                actions = {},
                navIcon = {},
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.padding(it)
        ) {
            if (listStranger.isEmpty()) {
                item {
                    Column(
                        modifier = modifier.fillParentMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Belum ada permintaan pertemanan :(")
                    }
                }
            }
            items(listStranger,key = { stranger->stranger.userId}) { stranger ->
                CardFriendRequest(
                    name = stranger.fullName,
                    message = stranger.message,
                    profilePicture = stranger.profilePicture,
                    requestAt = stranger.createdAt.toString(),
                    onAccept = {
                        action(FriendRequestAction.AcceptFriendRequest(stranger))
                    },
                    onReject = {
                        action(FriendRequestAction.RejectFriendRequest(stranger))
                    }
                )
            }
        }
    }


}

@Preview
@Composable
fun PreviewFriendRequestScreen() {
    AndroidRustTheme {
        FriendRequestScreen(
            nav = rememberNavController()
        )
    }
}