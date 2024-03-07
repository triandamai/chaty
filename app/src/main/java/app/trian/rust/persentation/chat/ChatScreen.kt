package app.trian.rust.persentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.rust.common.Either
import app.trian.rust.component.JumpToBottom
import app.trian.rust.data.dataSource.local.entity.ChatWithDetail
import app.trian.rust.persentation.chat.component.ChannelNameBar
import app.trian.rust.persentation.chat.component.ChatBubbleHeader
import app.trian.rust.persentation.chat.component.ChatInput
import app.trian.rust.persentation.chat.component.ChatItem
import app.trian.rust.ui.theme.AndroidRustTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

private val JumpToBottomThreshold = 56.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    nav: NavHostController = rememberNavController(),
    chats: SnapshotStateList<ChatWithDetail> = SnapshotStateList(),
    stateFlow: Flow<ChatState> = flowOf(),
    effectFlow: Flow<Either<ChatEffect>> = flowOf(),
    action: (ChatAction) -> Unit = {},
) {
    val state by stateFlow.collectAsState(initial = ChatState())

    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()
    // Jump to bottom button shows up when user scrolls past a threshold.
    // Convert to pixels:
    val jumpThreshold = with(LocalDensity.current) { JumpToBottomThreshold.toPx() }

    // Show the button if the first visible item is not the first one or if the offset is
    // greater than the threshold.
    val jumpToBottomButtonEnabled by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex != 0 || scrollState.firstVisibleItemScrollOffset > jumpThreshold }
    }

    LaunchedEffect(key1 = Unit, block = {
        action(ChatAction.GetDetailRoom)
        action(ChatAction.GetChatByRoomId)
    })

    Scaffold(
        topBar = {
            ChannelNameBar(
                channelName = "#Topicchanne",
                channelMembers = 20,
                onNavIconPressed = {},
                scrollBehavior = scrollBehavior,
            )
        },
        // Exclude ime and navigation bar padding so this can be added by the UserInput composable
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            ChatInput(
                onMessageSent = { content ->
                    action(ChatAction.SubmitChat(content))
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                // let this element handle the padding so that the elevation is shown behind the
                // navigation bar
                modifier = modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = modifier) {
                LazyColumn(
                    reverseLayout = true,
                    state = scrollState,
                    modifier = Modifier
                        .testTag("")
                        .fillMaxSize()
                ) {
                    itemsIndexed(
                        chats,
                        key = { _, key ->
                            key.chat.chatId
                        }
                    ) { index, chat ->
                        val nextChat = chats.getOrNull(index + 1)?.chat
                        val prevChat = chats.getOrNull(index - 1)?.chat
                        val author = state.currentUser

                        val isFirstMessageByAuthor = prevChat?.authorId != chat.chat.authorId
                        val isLastMessageByAuthor = nextChat?.authorId != chat.chat.authorId


                        val showDivider =
                            nextChat?.createdAt?.format(formatter) != chat.chat.createdAt.format(
                                formatter
                            )
                        if (showDivider) {
                            ChatBubbleHeader(chat.chat.createdAt.format(formatter))
                        }

                        ChatItem(
                            onAuthorClick = { name -> },
                            authorId = chat.chat.authorId,
                            isUserMe = chat.chat.authorId == state.currentUser,
                            senderName = if (chat.chat.authorId == state.currentUser) "Me" else "",
                            body = chat.chat.content,
                            attachment = chat.chatAttachment,
                            profilePicture = "",
                            timestamp = "02:03",
                            isFirstMessageByAuthor = isFirstMessageByAuthor,
                            isLastMessageByAuthor = isLastMessageByAuthor
                        )
                    }
                }


                JumpToBottom(
                    // Only show if the scroller is not at the bottom
                    enabled = jumpToBottomButtonEnabled,
                    onClicked = {
                        scope.launch {
                            scrollState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSplashScreen() {
    AndroidRustTheme {
        ChatScreen()
    }
}