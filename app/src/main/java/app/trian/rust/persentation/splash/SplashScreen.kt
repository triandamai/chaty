package app.trian.rust.persentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.rust.common.Either
import app.trian.rust.common.On
import app.trian.rust.ui.theme.AndroidRustTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    nav: NavHostController = rememberNavController(),
    stateFlow: Flow<SplashState> = flowOf(),
    effectFlow: Flow<Either<SplashEffect>> = flowOf(),
    action: (SplashAction) -> Unit = {},
) {

    val state by stateFlow.collectAsState(initial = SplashState())
    val effect by effectFlow.collectAsState(initial = Either.left())

    LaunchedEffect(key1 = Unit, block = {
        action(SplashAction.CheckSession)
    })
    On(type = SplashEffect.NavigateToHome::class.java, effect = effect, cb = {
        nav.navigate("home"){
            launchSingleTop = true
            popUpTo("splash"){
                inclusive = true
            }
        }
    })
    On(type = SplashEffect.NavigateToSignIn::class.java, effect = effect, cb = {
        nav.navigate("sign-in"){
            launchSingleTop = true 
            popUpTo("splash"){
                inclusive=true
            }
        }
    })

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Splash Screen")
    }

}

@Preview
@Composable
fun PreviewSplashScreen() {
    AndroidRustTheme {
        SplashScreen()
    }
}