package app.trian.rust.persentation.splash

import app.trian.rust.common.BaseViewModel
import app.trian.rust.data.Response
import app.trian.rust.data.domain.CheckSessionUseCase
import app.trian.rust.persentation.signIn.SignInStateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkSessionUseCase: CheckSessionUseCase,
) : BaseViewModel<SplashState,SplashStateImpl, SplashAction, SplashEffect>(
    SplashStateImpl()
) {

    override fun onAction() {
        on(SplashAction.CheckSession::class.java) {
            when (val isLoggedIn = checkSessionUseCase()) {
                is Response.Error -> {
                    sendEffect(SplashEffect.NavigateToSignIn)
                }

                is Response.Result -> {
                    if (isLoggedIn.data) {
                        sendEffect(SplashEffect.NavigateToHome)
                    } else {
                        sendEffect(SplashEffect.NavigateToSignIn)
                    }
                }
            }
        }
    }
}