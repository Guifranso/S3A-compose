package br.com.arcom.s3a.ui.checagem


import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.arcom.s3a.util.ImageRotationUtil
import br.com.arcom.s3a.util.asNumber
import br.com.arcom.s3a.util.getImageUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlin.reflect.KFunction0

@ExperimentalMaterial3Api
@Composable
fun ChecagemRoute(
    onBackClick: () -> Unit,
    viewModel: ChecagemViewModel = hiltViewModel(),
) {
    val checagemUiState = viewModel.checagemUiState.collectAsStateWithLifecycle()

    ChecagemScreen(
        onBackClick = onBackClick,
        recognizeText = viewModel::recognizeText,
        checagemUiState = checagemUiState.value,
        sendChecagem = viewModel::sendChecagem,
        validado = checagemUiState.value.validado
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun ChecagemScreen(
    onBackClick: () -> Unit,
    recognizeText: (Bitmap, String) -> Unit,
    checagemUiState: ChecagemUiState,
    sendChecagem: () -> Unit,
    validado: Boolean
) {
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current
    var file by remember { mutableStateOf<Pair<File, Uri>?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (file != null && success) {
                val bitmap = ImageRotationUtil.rotateAndCompressImage(file!!.first)
                recognizeText(bitmap, input)
            }
        }
    )

    fun startCamera() {
        scope.launch {
            file = getImageUri(context)
            cameraLauncher.launch(file!!.second)
        }
    }

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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {

                Text("Digite a kilometragem indicada no hodômetro")
                OutlinedTextField(
                    value = input,
                    onValueChange = {
                        if (it.asNumber()) {
                            input = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                )
                Button(
                    onClick = {
                        if (permissions.allPermissionsGranted) {
                            startCamera()
                        } else {
                            permissions.launchMultiplePermissionRequest()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .requiredHeight(56.dp),
                    enabled = input.isNotEmpty(),
                ) { Text("Tirar foto") }

                Button(
                    onClick = {
                        sendChecagem()
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .requiredHeight(56.dp),
                    enabled = validado,
                ) { Text("Enviar") }
            }
        }
    }
}


