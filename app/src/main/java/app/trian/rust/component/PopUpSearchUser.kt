package app.trian.rust.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.trian.rust.ui.theme.AndroidRustTheme

@Composable
fun PopUpSearchUser(
    modifier: Modifier = Modifier,
    show: Boolean,
    value: String,
    onChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSend: () -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(
                        horizontal = 10.dp,
                        vertical = 10.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = modifier.height(15.dp))
                Text(
                    text = "Cari pengguna",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = modifier.height(10.dp))
                OutlinedTextField(
                    value = value,
                    onValueChange = onChange,
                    placeholder = {
                        Text(text = "Cari username pengguna")
                    }
                )
                Spacer(modifier = modifier.height(10.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onSend) {
                        Text(text = "Cari")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPopUpSearchUser() {
    AndroidRustTheme {
        PopUpSearchUser(
            show = true,
            value = "",
            onChange = {},
            onDismiss = {},
            onSend = {}
        )
    }
}