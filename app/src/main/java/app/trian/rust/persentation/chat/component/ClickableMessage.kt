package app.trian.rust.persentation.chat.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.trian.rust.data.contentRandom
import app.trian.rust.ui.theme.AndroidRustTheme

@Composable
fun ClickableMessage(
    body:String,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(
        text = body,
        primary = isUserMe
    )

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewClickableMessage() {
    AndroidRustTheme {
        ClickableMessage(
            body = contentRandom.random(),
            isUserMe = false,
            authorClicked = {}
        )
    }
}