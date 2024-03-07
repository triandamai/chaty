package app.trian.rust.persentation.splash

sealed interface SplashAction {
    data object None : SplashAction
    data object CheckSession : SplashAction
    data class Submit(val name: String) : SplashAction
}

sealed interface SplashEffect {
    data object None : SplashEffect

    data object NavigateToHome:SplashEffect
    data object NavigateToSignIn:SplashEffect
}
