package app.trian.rust.persentation.chat.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.trian.rust.data.dataSource.local.entity.ChatAttachment
import app.trian.rust.ui.theme.AndroidRustTheme

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@Composable
fun ChatBubble(
    body: String,
    attachment: List<ChatAttachment>,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column(
        modifier = Modifier.padding(end = 10.dp)
    ) {
        Surface(
            color = backgroundBubbleColor,
            shape = ChatBubbleShape
        ) {
            ClickableMessage(
                body = body,
                isUserMe = isUserMe,
                authorClicked = authorClicked
            )
        }

//        message.image?.let {
//            Spacer(modifier = Modifier.height(4.dp))
//            Surface(
//                color = backgroundBubbleColor,
//                shape = ChatBubbleShape
//            ) {
//                Image(
//                    painter = painterResource(it),
//                    contentScale = ContentScale.Fit,
//                    modifier = Modifier.size(160.dp),
//                    contentDescription = stringResource(id = R.string.attached_image)
//                )
//            }
//        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true,

    )
@Composable
fun PreviewChatBubble() {
    AndroidRustTheme {
        Column(
            modifier = Modifier.padding(all = 10.dp)
        ) {
            ChatBubble(
                isUserMe = false,
                body = "",
                attachment = listOf(),
                authorClicked = {}
            )
        }
    }
}
