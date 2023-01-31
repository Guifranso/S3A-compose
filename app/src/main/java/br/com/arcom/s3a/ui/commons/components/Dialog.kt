package br.com.arcom.s3a.ui.commons.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DialogConfirmacao(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    title: String? = null,
    text: @Composable (() -> Unit)? = null,
    closeDialog: () -> Unit,
    confirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { closeDialog() },
        title = { if (title != null) Text(text = title) },
        confirmButton = {
            TextButton(
                onClick = {
                    confirmClick()
                    closeDialog()
                },
            ) {
                Text(
                    text = "confirmar",
                    color = contentColor
                )
            }
        },
        text = if (text != null) {
            { text() }
        } else null,
        dismissButton = {
            TextButton(
                onClick = { closeDialog() }
            ) {
                Text(
                    text = "cancelar",
                    color = contentColor
                )
            }
        },
        containerColor = containerColor,
        modifier = modifier
    )
}