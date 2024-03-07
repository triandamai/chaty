package app.trian.rust.persentation.signIn

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.rust.common.Either
import app.trian.rust.common.GoogleAuthContract
import app.trian.rust.common.On
import app.trian.rust.component.PopUpLoading
import app.trian.rust.ui.theme.AndroidRustTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    nav: NavHostController,
    stateFlow: Flow<SignInState> = flowOf(),
    effectFlow: Flow<Either<SignInEffect>> = flowOf(),
    action: (SignInAction) -> Unit = {},
) {
    val state by stateFlow.collectAsState(initial = SignInState())
    val effect by effectFlow.collectAsState(initial = Either.left())
    val ctx = LocalContext.current
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = GoogleAuthContract(),
        onResult = {
            action(SignInAction.SingInWithGoogle(it))
        }
    )

    On(type = SignInEffect.ShowToast::class.java, effect = effect, cb = {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG)
            .show()
    })
    On(type = SignInEffect.NavigateToMain::class.java, effect = effect, cb = {
        nav.navigate("home") {
            launchSingleTop = true
            popUpTo("sign-in") {
                inclusive = true
            }
        }
    })

    PopUpLoading(show = state.showLoading)
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .safeContentPadding()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Masuk",
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = modifier.height(10.dp))
            Text(text = "Masuk dengan akun kamu.")
        }
        Column {
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = state.emailState,
                label = {
                    Text(text = "Email")
                },
                placeholder = {
                    Text(text = "Masukkan email kamu")
                },
                onValueChange = {
                    action(SignInAction.OnEmailChange(it))
                }
            )
        }
        Column {
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    action(SignInAction.SignInBasic)
                }
            ) {
                Text(text = "Masuk")

            }

            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    googleSignInLauncher.launch(1)
                }
            ) {
                Text(text = "Lanjutkan dengan google")

            }
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
fun PreviewSplashScreen() {
    AndroidRustTheme {
        SignInScreen(
            nav = rememberNavController()
        )
    }
}