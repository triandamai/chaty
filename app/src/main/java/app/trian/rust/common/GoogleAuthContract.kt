package app.trian.rust.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import app.trian.rust.data.Response
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

class GoogleAuthContract : ActivityResultContract<Int, Response<Task<GoogleSignInAccount>>>() {
    override fun createIntent(context: Context, input: Int): Intent {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("721289871304-0q5kb036hg1a18r7ldek81rucnpn4bi7.apps.googleusercontent.com")
            .requestId()
            .requestProfile()
            .build()

        val gsi = GoogleSignIn.getClient(
            context,
            gso
        )
        gsi.signOut()
        return gsi.signInIntent
    }


    override fun parseResult(resultCode: Int, intent: Intent?): Response<Task<GoogleSignInAccount>> {
        return when (resultCode) {
            Activity.RESULT_OK -> {
                val data = GoogleSignIn.getSignedInAccountFromIntent(intent)
                return Response.Result(data)
            }

            else -> {
                Response.Error(
                    intent?.extras?.keySet()?.map {
                        intent.extras?.getString(it.toString()).toString()
                    }.toString(), resultCode)
            }
        }
    }


}