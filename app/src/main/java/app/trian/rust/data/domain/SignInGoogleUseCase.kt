package app.trian.rust.data.domain

import app.trian.rust.data.Response
import app.trian.rust.data.repositories.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class SignInGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        result: Response<Task<GoogleSignInAccount>>,
    ): Response<FirebaseUser> {
        return authRepository.signInGoogle(result)
    }
}