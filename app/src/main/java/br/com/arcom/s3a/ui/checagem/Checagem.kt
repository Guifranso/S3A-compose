package br.com.arcom.s3a.ui.checagem


import android.Manifest
import android.graphics.Bitmap
import android.location.Location
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
import br.com.arcom.s3a.util.LocationHelper
import br.com.arcom.s3a.util.asNumber
import br.com.arcom.s3a.util.getImageUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
        sendChecagem = viewModel::sendChecagem
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalMaterial3Api
@Composable
fun ChecagemScreen(
    onBackClick: () -> Unit,
    recognizeText: (Bitmap, String) -> Unit,
    checagemUiState: ChecagemUiState,
    sendChecagem: KFunction0<Unit>
) {
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }
    var file by remember { mutableStateOf<Pair<File, Uri>?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (file != null && success) {
                bitmap = ImageRotationUtil.rotateAndCompressImage(file!!.first)
            }
        }
    )

    fun startCamera() {
        scope.launch {
            location = LocationHelper(context).displayDistance()
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

    LaunchedEffect(location, bitmap) {
        if (location != null && bitmap != null) {
            recognizeText(bitmap!!, input)
        }
    }

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
                    enabled = input.isNotEmpty(),
                ) { Text("Enviar") }
            }
        }
    }
}


