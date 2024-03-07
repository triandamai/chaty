package app.trian.rust.data.repositories

import app.trian.rust.data.Response
import app.trian.rust.model.UserModel
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class AuthRepository @Inject constructor() {
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore

    suspend fun signInGoogle(
        result: Response<Task<GoogleSignInAccount>>,
    ): Response<FirebaseUser> {
        return when (result) {
            is Response.Error -> result
            is Response.Result -> {
                return try {
                    val data = result.data.await()
                    val signInResult = GoogleAuthProvider.getCredential(data.idToken, null)

                    val authResult = auth.signInWithCredential(signInResult).await()
                    if (authResult.user == null) {
                        Response.error("Sign In failed")
                    }

                    val account = authResult.user!!

                    val currentDate = LocalDateTime.now()
                    val user = UserModel(
                        userId = account.uid,
                        username = account.email.orEmpty().split("@")[0],
                        fullName = account.displayName.orEmpty(),
                        profilePicture = account.photoUrl.toString(),
                        bio = "Pengguna baru",
                        banner = "",
                        createdAt = currentDate.toEpochSecond(ZoneOffset.UTC),
                        updatedAt = currentDate.toEpochSecond(ZoneOffset.UTC)
                    )

                    val userExist = firestore
                        .collection(UserRepository.USERS)
                        .document(account.uid)
                        .get()
                        .await()

                    if (!userExist.exists()) {
                        firestore.collection(UserRepository.USERS)
                            .document(account.uid)
                            .set(user, SetOptions.merge())
                            .await()
                    }
                    Response.success(account)

                } catch (e: GoogleAuthException) {
                    Response.error(e.message.orEmpty())
                }
            }
        }
    }
}