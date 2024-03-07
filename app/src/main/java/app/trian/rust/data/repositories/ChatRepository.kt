package app.trian.rust.data.repositories

import android.util.Log
import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.MainDatabase
import app.trian.rust.data.dataSource.local.entity.Chat
import app.trian.rust.data.dataSource.local.entity.ChatAttachment
import app.trian.rust.data.dataSource.local.entity.ChatWithDetail
import app.trian.rust.data.dataSource.local.entity.RoomWithMember
import app.trian.rust.model.ChatModel
import app.trian.rust.model.RoomModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val db: MainDatabase,
) {
    companion object{
        const val CHAT = "CHAT"
        const val CHATS = "CHATS"
    }

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    suspend fun getListChatByRoom(roomId: String): List<ChatWithDetail> =
        withContext(Dispatchers.IO) {
            db.chatDao().getChatsByRoom(roomId)
        }

    suspend fun getChatWithDetail(chatId: String): Response<ChatWithDetail> =
        withContext(Dispatchers.IO) {
            val data = db.chatDao().getChatById(chatId)

            if (data != null) {
                Response.success(data)
            } else {
                Response.error()
            }
        }

    suspend fun insertChatFromCloud(chat: ChatModel): Chat = withContext(Dispatchers.IO) {
        val data = Chat(
            chatId = chat.chatId,
            roomId = chat.roomId,
            authorId = chat.authorId,
            content = chat.content,
            createdAt = LocalDateTime.ofEpochSecond(chat.createdAt, 0, ZoneOffset.UTC),
            updatedAt = LocalDateTime.ofEpochSecond(chat.updatedAt, 0, ZoneOffset.UTC)
        )
        db.chatDao().insertChat(data)
        data
    }

    suspend fun deleteRoomFromCloud(chatModel: ChatModel): Int = withContext(Dispatchers.IO) {
        db.chatDao().deleteChat(chatModel.chatId)
    }


    suspend fun insertAttachment(attachments: List<ChatAttachment>) = withContext(Dispatchers.IO) {
        db.chatAttachmentDao().insertAttachment(*attachments.toTypedArray())
    }

    suspend fun sendChatByRoom(
        roomId: String,
        sendTo: List<String>,
        content: String,
    ) = withContext(Dispatchers.IO) {
        val currentUser = auth.currentUser ?: return@withContext Response.error("Belum login")

        val ref = firestore.collection("CHAT")
        val currentTime = LocalDateTime.now()
        var chat = ChatModel(
            chatId = ref.document().id, // automated generate
            roomId = roomId,
            authorId = currentUser.uid,
            content = content,
            createdAt = currentTime.toEpochSecond(ZoneOffset.UTC),
            updatedAt = currentTime.toEpochSecond(ZoneOffset.UTC)
        )
        sendTo.filter {
            it != currentUser.uid
        }.forEach {
            Log.e("KKK",it)
            val path = ref.document(it)
                .collection("CHAT")
            val chatId = path.document().id
            chat = chat.copy(
                chatId = chatId
            )
            path.document(chatId).set(chat, SetOptions.merge()).await()
        }

        val chatEntity = insertChatFromCloud(chat)
        Response.success(
            ChatWithDetail(
                chat = chatEntity,
                chatAttachment = listOf()
            )
        )

    }
}