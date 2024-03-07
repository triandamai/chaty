package app.trian.rust

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import app.trian.rust.data.repositories.ChatRepository
import app.trian.rust.data.repositories.RoomRepository
import app.trian.rust.data.repositories.UserRepository
import app.trian.rust.model.ChatModel
import app.trian.rust.model.RoomModel
import app.trian.rust.model.UserModel
import app.trian.rust.persentation.addFriend.AddFriendScreen
import app.trian.rust.persentation.addFriend.AddFriendViewModel
import app.trian.rust.persentation.chat.ChatAction
import app.trian.rust.persentation.chat.ChatScreen
import app.trian.rust.persentation.chat.ChatViewModel
import app.trian.rust.persentation.friendList.FriendListAction
import app.trian.rust.persentation.friendList.FriendListScreen
import app.trian.rust.persentation.friendList.FriendListViewModel
import app.trian.rust.persentation.friendRequest.FriendRequestScreen
import app.trian.rust.persentation.friendRequest.FriendRequestViewModel
import app.trian.rust.persentation.home.HomeAction
import app.trian.rust.persentation.home.HomeScreen
import app.trian.rust.persentation.home.HomeViewModel
import app.trian.rust.persentation.signIn.SignInScreen
import app.trian.rust.persentation.signIn.SignInViewModel
import app.trian.rust.persentation.splash.SplashScreen
import app.trian.rust.persentation.splash.SplashViewModel
import app.trian.rust.ui.theme.AndroidRustTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ConcurrentHashMap

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var roomListener: ConcurrentHashMap<String, OnRoomListener> = ConcurrentHashMap()
    private var chatListener: ConcurrentHashMap<String, OnChatListener> = ConcurrentHashMap()
    private var friendListener: ConcurrentHashMap<String, OnFriendListener> = ConcurrentHashMap()

    private val firestore = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var navHostController: NavHostController
    private val mainViewModel: MainViewModel by viewModels()

    interface OnFriendListener {
        fun onFriendAdded(user: UserModel)
        fun onFriendRemoved(user: UserModel)
        fun onFriendChanged(user: UserModel)
    }

    interface OnRoomListener {
        fun onRoomChanged(room: RoomModel)
        fun onRoomRemoved(room: RoomModel)
        fun onRoomAdded(room: RoomModel)
    }

    interface OnChatListener {
        fun onChatAdded(chat: ChatModel)
        fun onChatRemoved(chat: ChatModel)
        fun onChatChanged(chat: ChatModel)
    }

    private fun addChatListener(page: String, callback: OnChatListener) {
        chatListener[page] = callback
    }

    private fun removeChatListener(page: String) {
        chatListener.remove(page)
    }

    private fun addRoomListener(page: String, callback: OnRoomListener) {
        roomListener[page] = callback
    }

    private fun removeRoomListener(page: String) {
        roomListener.remove(page)
    }

    private fun addFriendListener(page: String, callback: OnFriendListener) {
        friendListener[page] = callback
    }

    private fun removeFriendListener(page: String) {
        friendListener.remove(page)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val app = AntiDebugger()

        auth.addAuthStateListener {
            if (it.currentUser != null) {
                listenRoomChanges()
                listenChatChanges()
                listenFriendChanges()
            }
        }
        setContent {
            navHostController = rememberNavController()
            AndroidRustTheme(dynamicColor = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navHostController,
                        startDestination = MainConstant.Routes.SPLASH
                    ) {
                        composable(MainConstant.Routes.SPLASH) {
                            val viewModel = hiltViewModel<SplashViewModel>()
                            SplashScreen(
                                nav = navHostController,
                                stateFlow = viewModel.uiState,
                                effectFlow = viewModel.onEffect,
                                action = viewModel::invokeAction
                            )
                        }
                        composable(MainConstant.Routes.SIGN_IN) {
                            val viewModel = hiltViewModel<SignInViewModel>()
                            SignInScreen(
                                nav = navHostController,
                                stateFlow = viewModel.uiState,
                                effectFlow = viewModel.onEffect,
                                action = viewModel::invokeAction
                            )
                        }
                        composable(MainConstant.Routes.ADD_FRIEND) {
                            val viewModel = hiltViewModel<AddFriendViewModel>()
                            AddFriendScreen(
                                nav = navHostController,
                                listUser = viewModel.listUser,
                                stateFlow = viewModel.uiState,
                                effectFlow = viewModel.onEffect,
                                action = viewModel::invokeAction
                            )
                        }
                        composable(MainConstant.Routes.FRIEND_REQUEST) {
                            val viewModel = hiltViewModel<FriendRequestViewModel>()
                            FriendRequestScreen(
                                nav = navHostController,
                                listStranger = viewModel.listStranger,
                                stateFlow = viewModel.uiState,
                                effectFlow = viewModel.onEffect,
                                action = viewModel::invokeAction
                            )
                        }
                        composable(MainConstant.Routes.FRIEND_LIST) {
                            val viewModel = hiltViewModel<FriendListViewModel>()
                            LaunchedEffect(key1 = Unit) {
                                addFriendListener(
                                    MainConstant.Routes.FRIEND_LIST,
                                    object : OnFriendListener {
                                        override fun onFriendAdded(user: UserModel) =
                                            viewModel.invokeAction(
                                                FriendListAction.OnFriendAdded(
                                                    user
                                                )
                                            )

                                        override fun onFriendRemoved(user: UserModel) =
                                            viewModel.invokeAction(
                                                FriendListAction.OnFriendRemove(
                                                    user
                                                )
                                            )

                                        override fun onFriendChanged(user: UserModel) =
                                            viewModel.invokeAction(
                                                FriendListAction.OnFriendChanged(
                                                    user
                                                )
                                            )

                                    })
                            }

                            DisposableEffect(key1 = Unit) {
                                onDispose {
                                    removeFriendListener(MainConstant.Routes.FRIEND_LIST)
                                }
                            }

                            FriendListScreen(
                                nav = navHostController,
                                listFriend = viewModel.listFriend,
                                stateFlow = viewModel.uiState,
                                effectFlow = viewModel.onEffect,
                                action = viewModel::invokeAction
                            )
                        }
                        composable(MainConstant.Routes.HOME) {
                            val viewModel = hiltViewModel<HomeViewModel>()
                            LaunchedEffect(key1 = Unit, block = {
                                addRoomListener(MainConstant.Routes.HOME, object : OnRoomListener {
                                    override fun onRoomChanged(room: RoomModel) =
                                        viewModel.invokeAction(HomeAction.OnRoomChanged(room))

                                    override fun onRoomRemoved(room: RoomModel) =
                                        viewModel.invokeAction(HomeAction.OnRoomRemove(room))

                                    override fun onRoomAdded(room: RoomModel) =
                                        viewModel.invokeAction(HomeAction.OnRoomAdded(room))
                                })

                            })
                            DisposableEffect(key1 = Unit, effect = {
                                onDispose {
                                    removeRoomListener(MainConstant.Routes.HOME)
                                }
                            })
                            HomeScreen(
                                nav = navHostController,
                                listRoom = viewModel.listRoom,
                                stateFlow = viewModel.uiState,
                                effectFlow = viewModel.onEffect,
                                action = viewModel::invokeAction
                            )
                        }
                        composable(
                            "${MainConstant.Routes.CHAT}/{roomId}",
                            arguments = listOf(
                                navArgument("roomId") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val viewModel = hiltViewModel<ChatViewModel>()
                            LaunchedEffect(key1 = Unit, block = {
                                addChatListener(MainConstant.Routes.CHAT, object : OnChatListener {
                                    override fun onChatAdded(chat: ChatModel) =
                                        viewModel.invokeAction(ChatAction.OnChatAdded(chat))

                                    override fun onChatRemoved(chat: ChatModel) =
                                        viewModel.invokeAction(ChatAction.OnChatRemove(chat))

                                    override fun onChatChanged(chat: ChatModel) =
                                        viewModel.invokeAction(ChatAction.OnChatChanged(chat))
                                })
                                addRoomListener(MainConstant.Routes.CHAT, object : OnRoomListener {
                                    override fun onRoomChanged(room: RoomModel) {}
                                    override fun onRoomRemoved(room: RoomModel) {}
                                    override fun onRoomAdded(room: RoomModel) {}
                                })

                            })
                            DisposableEffect(key1 = Unit, effect = {
                                onDispose {
                                    removeChatListener(MainConstant.Routes.CHAT)
                                    removeRoomListener(MainConstant.Routes.CHAT)
                                }
                            })
                            ChatScreen(
                                nav = navHostController,
                                chats = viewModel.listChat,
                                stateFlow = viewModel.uiState,
                                effectFlow = viewModel.onEffect,
                                action = viewModel::invokeAction
                            )
                        }
                    }

                }
            }
        }
    }

    private fun listenRoomChanges() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection(RoomRepository.ROOM)
                .whereArrayContains(RoomRepository.FIELD_MEMBER, currentUser.uid)
                .addSnapshotListener(this) { value, error ->
                    if (error == null) {
                        value?.documentChanges?.forEach {
                            val data = it.document.toObject(RoomModel::class.java)
                            val members = data.members.map {
                                var user: UserModel? = null
                                firestore.collection(UserRepository.USERS).document(it).get()
                                    .addOnCompleteListener {
                                        try {
                                            user = it.result.toObject(UserModel::class.java)
                                        } catch (_: Exception) {
                                        }
                                    }
                                user

                            }.filterNotNull()
                            when (it.type) {
                                DocumentChange.Type.ADDED -> {
                                    mainViewModel.insertRoom(data, members) {
                                        roomListener.forEach { cb -> cb.value.onRoomAdded(data) }
                                    }
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    mainViewModel.insertRoom(data, members) {
                                        roomListener.forEach { cb -> cb.value.onRoomChanged(data) }
                                    }
                                }

                                DocumentChange.Type.REMOVED -> {
                                    mainViewModel.deleteRoom(data) {
                                        roomListener.forEach { cb -> cb.value.onRoomRemoved(data) }
                                    }
                                }
                            }
                        }
                    }
                }

        }
    }

    private fun listenFriendChanges() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection(UserRepository.USERS)
                .document(currentUser.uid)
                .collection(UserRepository.FRIEND_LIST)
                .addSnapshotListener(this) { value, error ->
                    if (error != null) {
                        value?.documentChanges?.forEach {
                            val data = it.document.toObject(UserModel::class.java)
                            when (it.type) {
                                DocumentChange.Type.ADDED -> {
                                    mainViewModel.insertFriend(data) {
                                        friendListener.forEach { (_, cb) ->
                                            cb.onFriendAdded(data)
                                        }
                                    }
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    mainViewModel.insertFriend(data) {
                                        friendListener.forEach { (_, cb) ->
                                            cb.onFriendChanged(data)
                                        }
                                    }
                                }

                                DocumentChange.Type.REMOVED -> {
                                    mainViewModel.deleteFriend(data) {
                                        friendListener.forEach { (_, cb) ->
                                            cb.onFriendRemoved(data)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun listenChatChanges() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection(ChatRepository.CHAT)
                .document(currentUser.uid)
                .collection(ChatRepository.CHATS)
                .addSnapshotListener(this) { value, error ->
                    if (error != null) {
                        value?.documentChanges?.forEach {
                            val data = it.document.toObject(ChatModel::class.java)
                            when (it.type) {
                                DocumentChange.Type.ADDED -> {
                                    mainViewModel.insertChat(data) {
                                        chatListener.forEach { (_, cb) -> cb.onChatAdded(data) }
                                    }
                                }

                                DocumentChange.Type.MODIFIED -> {
                                    mainViewModel.insertChat(data) {
                                        chatListener.forEach { (_, cb) -> cb.onChatChanged(data) }
                                    }
                                }

                                DocumentChange.Type.REMOVED -> {
                                    mainViewModel.deleteChat(data) {
                                        chatListener.forEach { (_, cb) -> cb.onChatRemoved(data) }
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }
}