package app.trian.rust.data.domain

import app.trian.rust.data.Response
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CheckSessionUseCase @Inject constructor() {
    private val auth = Firebase.auth
    operator fun invoke(): Response<Boolean> {
        return if (auth.currentUser != null) Response.success(true)
        else Response.error()
    }
}