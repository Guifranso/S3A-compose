package br.com.arcom.s3a.ui.checagem

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.arcom.s3a.R
import br.com.arcom.s3a.data.model.TipoChecagem
import br.com.arcom.s3a.ui.commons.components.S3aOutlinedTextField
import br.com.arcom.s3a.ui.theme.S3aRoundedShape
import br.com.arcom.s3a.util.asNumber
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuilometragemContent(
    tipoChecagem: TipoChecagem,
    fields: ChecagemFields,
    hideKeyboard: () -> Unit
) {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(id = R.drawable.ic_car),
            contentDescription = "Image travel",

            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(200.dp)
                .clip(S3aRoundedShape),
            contentScale = ContentScale.Crop
        )

        Surface(
            shape = S3aRoundedShape, color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FactCheck,
                    contentDescription = "Icone checagem",
                    modifier = Modifier.requiredSize(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = tipoChecagem.descricao),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Text(
            style = MaterialTheme.typography.labelMedium,
            text = stringResource(id = R.string.quilometragem_odometro),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 16.dp)
        )

        S3aOutlinedTextField(
            value = if (fields.km != null) fields.km.toString() else "",
            onValueChange = {
                if (it.asNumber()) {
                    fields.km = it.toLongOrNull()
                }
            },
            label = stringResource(id = R.string.km),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 4.dp),
            keyboardActions = KeyboardActions(onDone = {
                hideKeyboard()
            }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FotoContent(tipoChecagem: TipoChecagem, fields: ChecagemFields, startCamera: () -> Unit) {

    val permissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
        ),
        onPermissionsResult = {
            if (it.entries.all { map -> map.value }) {
                startCamera()
            }
        }
    )

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(id = R.drawable.ic_velocimeter),
            contentDescription = "Image travel",

            modifier = Modifier
                .requiredSize(150.dp),
            contentScale = ContentScale.Crop
        )

        Surface(
            shape = S3aRoundedShape, color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FactCheck,
                    contentDescription = "Icone checagem",
                    modifier = Modifier.requiredSize(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = tipoChecagem.descricao),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Surface(
            shape = S3aRoundedShape, color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 16.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = "Icone carro",
                    modifier = Modifier.requiredSize(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = stringResource(id = R.string.quilometragem_variavel, fields.km ?: 0),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        if (fields.validado) {
            Surface(
                shape = S3aRoundedShape, color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Icone check",
                        modifier = Modifier.requiredSize(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = stringResource(id = R.string.foto_vericada),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        } else {
            Surface(
                onClick = {
                    if (permissions.allPermissionsGranted) {
                        startCamera()
                    } else {
                        permissions.launchMultiplePermissionRequest()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 16.dp),
                shape = S3aRoundedShape,
                color = MaterialTheme.colorScheme.secondary,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = stringResource(id = R.string.tirar_foto),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Arrow right",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                            .padding(12.dp)
                    )
                }
            }
        }

    }
}
