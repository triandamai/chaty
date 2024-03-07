package app.trian.rust.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.trian.rust.R
import app.trian.rust.ui.theme.AndroidRustTheme
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun CardFriendRequest(
    modifier: Modifier = Modifier,
    name: String = "",
    message: String = "",
    requestAt: String = "",
    profilePicture: String = "",
    onAccept: () -> Unit = {},
    onReject: () -> Unit = {},
) {
    val footer = 40.dp
    val bodyHeight = 100.dp
    val height = (bodyHeight + footer)
    val ctx = LocalContext.current
    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest
            .Builder(ctx)
            .data(profilePicture)
            .placeholder(R.drawable.ic_launcher_background)
            .build()
    )
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .padding(
                    vertical = 10.dp,
                    horizontal = 10.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(fraction = 0.7f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    modifier = modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(6.dp),
                ) {
                    Text(
                        text = requestAt,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "",
                    modifier = modifier
                        .size(bodyHeight - 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Row(
                    modifier = modifier.width(bodyHeight - 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = onReject) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(onClick = onAccept) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewCardFriendRequest() {
    AndroidRustTheme {
        LazyColumn(content = {
            items(4) {
                CardFriendRequest(
                    name = "Trian Damai",
                    message = "Sometimes do front end, sometimes do backend, mostly lying horizontally in the couch",
                    requestAt = "9 Feb 2024"
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        })
    }
}