package br.com.arcom.s3a.ui.commons.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import br.com.arcom.s3a.R

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

@Composable
fun DialogInfo(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    text: String,
    closeDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { closeDialog() },
        title = {
            Text(
                text = stringResource(id = R.string.sobre_s3a),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    closeDialog()
                },
            ) {
                Text(
                    stringResource(id = R.string.ok),
                    color = contentColor
                )
            }
        },
        text = { Text(text = text) },
        containerColor = containerColor,
        modifier = modifier,
        icon = {
            Icon(imageVector = Icons.Filled.Info, contentDescription = "Icon info")
        }, iconContentColor = contentColor
    )
}