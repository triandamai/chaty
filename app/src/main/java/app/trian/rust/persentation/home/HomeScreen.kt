package app.trian.rust.persentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.PeopleAlt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.rust.common.Either
import app.trian.rust.component.CustomAppbar
import app.trian.rust.data.dataSource.local.entity.RoomWithMember
import app.trian.rust.persentation.chat.component.ChannelNameAppBar
import app.trian.rust.persentation.chat.component.ChannelNameBar
import app.trian.rust.ui.theme.AndroidRustTheme
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    nav: NavHostController = rememberNavController(),
    listRoom: SnapshotStateList<RoomWithMember> = SnapshotStateList(),
    stateFlow: Flow<HomeState> = flowOf(),
    effectFlow: Flow<Either<HomeEffect>> = flowOf(),
    action: (HomeAction) -> Unit = {},
) {
    val state by stateFlow.collectAsState(initial = HomeState())
    val effect by effectFlow.collectAsState(initial = Either.left())

    LaunchedEffect(key1 = Unit, block = {
        action(HomeAction.GetListRoom)
    })

    Scaffold(
        topBar = {
            CustomAppbar(
                title = "Home",
                subtitle = "",
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "")
                    }
                    IconButton(onClick = {
                        nav.navigate("friend-list") {
                            launchSingleTop = true
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.PeopleAlt,
                            contentDescription = ""
                        )
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier.padding(it),
            content = {
                if (listRoom.isEmpty()) {
                    item {
                        Column(
                            modifier = modifier
                                .fillParentMaxHeight()
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "No Data", color = Color.White)
                        }
                    }
                }
                items(listRoom, key = { room -> room.room.roomId }) {
                    ItemRoom(
                        roomName = it.room.roomName,
                        lastChat = "Ini chat terakhir kita",
                        picture = "",
                        unreadChat = 3,
                        onClick = {
                            nav.navigate("chat/${it.room.roomId}")
                        }
                    )
                }
            })
    }
}

@Composable
fun ItemRoom(
    modifier: Modifier = Modifier,
    roomName: String,
    lastChat: String,
    picture: String,
    unreadChat: Int,
    onClick: () -> Unit,
) {

    val ctx = LocalContext.current
    val image = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(ctx)
            .data("https://i.pravatar.cc/300")
            .build()
    )

    Column {
        ListItem(
            modifier = modifier.clickable {
                onClick()
            },
            headlineContent = { Text(roomName) },
            supportingContent = { Text(lastChat) },
            trailingContent = { Text("$unreadChat") },
            leadingContent = {
                Image(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(42.dp)
                        .border(1.5.dp, MaterialTheme.colorScheme.tertiary, CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .clip(CircleShape)
                        .clickable(onClick = { }),
                    painter = image,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
        )
        HorizontalDivider()
    }
}

@Preview
@Composable
fun PreviewItemRoom() {
    AndroidRustTheme {
        ItemRoom(
            roomName = "",
            lastChat = "",
            picture = "",
            unreadChat = 1,
            onClick = {}
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    AndroidRustTheme {
        HomeScreen()
    }
}