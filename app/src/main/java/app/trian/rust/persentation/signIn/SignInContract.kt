package app.trian.rust.persentation.signIn

import app.trian.rust.data.Response
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

sealed interface SignInAction {
    data object None : SignInAction

    data class OnEmailChange(val value: String) : SignInAction

    data object SignInBasic : SignInAction

    data class SingInWithGoogle(val result: Response<Task<GoogleSignInAccount>>) : SignInAction
}

sealed interface SignInEffect {

    data class ShowToast(val message: String) : SignInEffect

    data object NavigateToMain : SignInEffect
}