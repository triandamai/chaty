package app.trian.rust.persentation.signIn

import androidx.compose.runtime.Immutable
import app.trian.generator.State


@State
@Immutable
data class SignInState(
    //global
    val showLoading: Boolean = false,
    val emailState: String = "",
)
