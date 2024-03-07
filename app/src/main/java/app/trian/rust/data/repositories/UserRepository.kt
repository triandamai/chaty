package app.trian.rust.data.repositories

import app.trian.rust.data.Response
import app.trian.rust.data.dataSource.local.MainDatabase
import app.trian.rust.data.dataSource.local.entity.Friend
import app.trian.rust.model.FriendRequestModel
import app.trian.rust.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: MainDatabase,
) {
    val auth: FirebaseAuth = Firebase.auth
    val firestore: FirebaseFirestore = Firebase.firestore

    companion object {
        const val FRIEND_REQUEST = "FRIEND_REQUEST"
        const val USERS = "USERS"
        const val FRIEND_LIST = "FRIEND_LIST"
        const val FIELD_USERNAME = "username"

    }

    suspend fun insertFriend(
        userModel: UserModel,
    ): Response<Friend> = withContext(Dispatchers.IO) {
        val friend = Friend(
            userId = userModel.userId,
            fullName = userModel.fullName,
            username = userModel.username,
            bio = userModel.bio,
            profilePicture = userModel.profilePicture,
            banner = userModel.banner,
            createdAt = LocalDateTime.ofEpochSecond(userModel.createdAt, 0, ZoneOffset.UTC),
            updatedAt = LocalDateTime.ofEpochSecond(userModel.createdAt, 0, ZoneOffset.UTC)
        )

        db.friendDao().insertFriend(friend)

        Response.success(friend)
    }

    suspend fun deleteFriend(userModel: UserModel)= withContext(Dispatchers.IO){
        db.friendDao().deleteFriend(userModel.userId)
    }

    suspend fun searchUser(
        query: String,
    ): Response<List<UserModel>> = withContext(Dispatchers.IO) {
        val user = auth.currentUser ?: return@withContext Response.error()

        val data = firestore.collection(USERS)
            .whereEqualTo(FIELD_USERNAME, query)
            .get()
            .await()
        val users = data.documents.map {
            return@map try {
                val transform = it.toObject(UserModel::class.java)
                transform
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }.filterNotNull().filter { item -> item.userId != user.uid }
        Response.success(users)
    }

    suspend fun getListFriend(): Response<List<UserModel>> = withContext(Dispatchers.IO) {
        val currentUser = auth.currentUser ?: return@withContext Response.error()
        val dataRef = firestore.collection(USERS)
            .document(currentUser.uid)
            .collection(FRIEND_LIST)
            .get()
            .await()

        val friends = dataRef.documents.map {
            return@map try {
                val transform = it.toObject(UserModel::class.java)
                transform
            } catch (e: Exception) {
                null
            }
        }.filterNotNull()


        Response.success(friends)
    }

    suspend fun addToFriendListRequest(
        userIdTarget: String,
        message: String,
    ): Response<FriendRequestModel> {
        val currentUser = auth.currentUser ?: return Response.error()
        val userModel = firestore.collection(USERS)
            .document(currentUser.uid)
            .get()
            .await()
            .toObject(UserModel::class.java) ?: return Response.error()

        val requestData = FriendRequestModel(
            userId = userModel.userId,
            fullName = userModel.fullName,
            profilePicture = userModel.profilePicture,
            bio = userModel.profilePicture,
            message = message,
            createdAt = userModel.createdAt,
            updatedAt = userModel.updatedAt
        )
        firestore.collection(USERS)
            .document(userIdTarget)
            .collection(FRIEND_REQUEST)
            .document(currentUser.uid)
            .set(requestData, SetOptions.merge())
            .await()

        return Response.success(requestData)
    }

    suspend fun getListFriendRequest(): Response<List<FriendRequestModel>> {
        val currentUser = auth.currentUser ?: return Response.error()

        val data = firestore.collection(USERS)
            .document(currentUser.uid)
            .collection(FRIEND_REQUEST)
            .get()
            .await()
            .documents.map {
                val data = it.toObject(FriendRequestModel::class.java)

                return@map data
            }.filterNotNull()

        return Response.success(data)

    }

    suspend fun acceptFriendListRequest(
        userIdTarget: String,
    ): Response<UserModel> {
        val currentUser = auth.currentUser ?: return Response.error("Kamu belum login")

        val myProfileRef = firestore.collection(USERS)
            .document(currentUser.uid)

        val friendProfileRef = firestore.collection(USERS)
            .document(userIdTarget)

        val myProfile = myProfileRef.get()
            .await()
            .toObject(UserModel::class.java)
            ?: return Response.error("Gagal mendapatkan informasi kamu")

        val friendProfile = friendProfileRef.get()
            .await()
            .toObject(UserModel::class.java)
            ?: return Response.error("Gagal mendapatkan informasi teman")


        myProfileRef
            .collection(FRIEND_LIST)
            .document(friendProfile.userId)
            .set(friendProfile, SetOptions.merge())
            .await()

        friendProfileRef
            .collection(FRIEND_LIST)
            .document(myProfile.userId)
            .set(myProfile, SetOptions.merge())
            .await()

        myProfileRef.collection(FRIEND_REQUEST)
            .document(userIdTarget)
            .delete()
            .await()

        return Response.success(friendProfile)
    }

    suspend fun rejectFriendListRequest(
        userIdTarget: String,
    ): Response<String> {
        val currentUser = auth.currentUser ?: return Response.error("Kamu belum login")
        val myProfileRef = firestore.collection(USERS)
            .document(currentUser.uid)

        myProfileRef.collection(FRIEND_REQUEST)
            .document(userIdTarget)
            .delete()
            .await()

        return Response.success(userIdTarget)
    }

}