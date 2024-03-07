package app.trian.rust.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.trian.rust.R
import app.trian.rust.ui.theme.AndroidRustTheme
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun BottomSheetSendFriendRequest(
    modifier: Modifier = Modifier,
    fullName: String = "",
    profilePicture: String = "",
    message:String,
    onMessageChange:(String)->Unit,
    onSend: () -> Unit = {},
) {
    val ctx = LocalContext.current
    val image = rememberAsyncImagePainter(
        model = ImageRequest.Builder(ctx)
            .data(profilePicture)
            .placeholder(R.drawable.ic_launcher_background)
            .build()
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 120.dp)
            .clip(
                RoundedCornerShape(
                    topStart = 12.dp,
                    topEnd = 12.dp
                )
            )
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = modifier.height(6.dp))
        Column {
            Box(
                modifier = modifier
                    .width(50.dp)
                    .height(6.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            )
        }
        Spacer(modifier = modifier.height(10.dp))
        Image(
            painter = image,
            contentDescription = "",
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = modifier.height(12.dp))
        Text(
            text = fullName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Kirim permintaan pertemanan ke ${fullName}",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = modifier.height(20.dp))
        OutlinedTextField(
            value = message,
            placeholder = {},
            onValueChange = onMessageChange,
            textStyle = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = modifier.height(15.dp))
        Button(onClick = {
            onSend()
        }) {
            Text(text = "Kirim")
        }
        Spacer(modifier = modifier.height(10.dp))
    }
}

@Preview
@Composable
fun PreviewBottomSheetSendFriendRequest() {
    AndroidRustTheme {
        BottomSheetSendFriendRequest(
            fullName = "Trian",
            message = "Hi, mari kita berteman.",
            onMessageChange = {}
        )
    }
}