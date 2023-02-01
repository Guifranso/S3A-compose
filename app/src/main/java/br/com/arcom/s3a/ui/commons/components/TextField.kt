package br.com.arcom.s3a.ui.commons.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import br.com.arcom.s3a.ui.theme.S3aRoundedShape
import br.com.arcom.s3a.ui.theme.lightColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun S3aOutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        label = { Text(text = label) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.background,
            textColor = MaterialTheme.colorScheme.onBackground,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.lightColor(),
            focusedLabelColor = MaterialTheme.colorScheme.onBackground
        ),
        keyboardActions = keyboardActions,
        shape = S3aRoundedShape
    )
}