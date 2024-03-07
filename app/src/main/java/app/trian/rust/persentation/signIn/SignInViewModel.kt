package app.trian.rust.persentation.signIn

import androidx.lifecycle.viewModelScope
import app.trian.rust.common.BaseViewModel
import app.trian.rust.common.executeAsFlow
import app.trian.rust.data.Response
import app.trian.rust.data.domain.SignInGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInGoogleUseCase: SignInGoogleUseCase,
) : BaseViewModel<SignInState,SignInStateImpl, SignInAction, SignInEffect>(
    SignInStateImpl()
) {

    override fun onAction() {
        on(SignInAction.SignInBasic::class.java) {
            sendEffect(SignInEffect.ShowToast("Random"))
        }
        on(SignInAction.OnEmailChange::class.java) {
            it.emailState = this.value
        }
        on(SignInAction.SingInWithGoogle::class.java) { state ->
            viewModelScope.launch {
                executeAsFlow { signInGoogleUseCase(result) }
                    .onStart {
                        state.showLoading = true
                    }
                    .onEach {
                        state.showLoading = false
                        when (it) {
                            is Response.Error -> {
                                instance send SignInEffect.ShowToast(it.message)
                            }

                            is Response.Result -> {
                                instance send SignInEffect.ShowToast("Selamat datang ${it.data.email}")
                                instance send SignInEffect.NavigateToMain
                            }
                        }
                    }
                    .collect()
            }
        }
    }

}