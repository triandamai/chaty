package app.trian.rust.persentation.chat.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.trian.rust.data.contentRandom
import app.trian.rust.data.dataSource.local.entity.ChatAttachment
import app.trian.rust.ui.theme.AndroidRustTheme
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun ChatItem(
    modifier: Modifier = Modifier,
    onAuthorClick: (String) -> Unit,
    authorId: String,
    profilePicture: String,
    senderName: String,
    timestamp: String,
    body: String,
    attachment: List<ChatAttachment>,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val ctx = LocalContext.current
    val image = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(ctx)
            .data(profilePicture)
            .build()
    )
    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            // Avatar
            Image(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .clickable(onClick = { onAuthorClick(authorId) })
                    .align(Alignment.Top),
                painter = image,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        Column(modifier = modifier) {
            if (isLastMessageByAuthor) {
                // Combine author and timestamp for a11y.
                Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
                    Text(
                        text = senderName,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .alignBy(LastBaseline)
                            .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.alignBy(LastBaseline),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            ChatBubble(
                body = body,
                attachment = attachment,
                isUserMe = isUserMe,
                authorClicked = onAuthorClick
            )
            if (isFirstMessageByAuthor) {
                // Last bubble before next author
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                // Between bubbles
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewChatItem() {
    AndroidRustTheme {
        ChatItem(
            onAuthorClick = {},
            authorId = "1",
            senderName = "Me",
            profilePicture = "",
            timestamp = "",
            isUserMe = false,
            body = contentRandom.random(),
            attachment = listOf(),
            isFirstMessageByAuthor = false,
            isLastMessageByAuthor = false
        )
    }
}
