package br.com.arcom.s3a.ui.cameraMlkit

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import br.com.arcom.s3a.ui.commons.LocationHelper
import br.com.arcom.s3a.util.ImageRotationUtil
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.FirebaseApp
import java.io.ByteArrayOutputStream


import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

@ExperimentalMaterial3Api
@Composable
fun CameraMlkitRoute(
    onBackClick: () -> Unit,
    viewModel: CameraMlkitViewModel = hiltViewModel(),
) {

    CameraMlkitScreen(
        onBackClick = onBackClick,

        )
}

@ExperimentalMaterial3Api
@Composable
fun CameraMlkitScreen(
    onBackClick: () -> Unit,

    ) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var input by remember { mutableStateOf("") }
                val pattern = remember { Regex("^\\d+\$") }

                Text("Digite a kilometragem indicada no hodômetro")
                Inputkm(
                    value = input,
                    onChange = {
                        if (it.isEmpty() || it.matches(pattern)) {
                            input = it
                        }
                    }
                )
                LocationButton(input, input != "")
            }
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationButton(inputKm: String, b: Boolean) {
    val context = LocalContext.current
    var location by remember { mutableStateOf<Location?>(null) }
    var uri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val permissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA
        )
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (uri != null && success) {
                val file = File(context.filesDir, "${LocalDateTime.now()}.jpg")
                if (!file.exists()) {
                    file.createNewFile()
                }
                val inputStream = context.contentResolver.openInputStream(uri!!)
                val outputStream = FileOutputStream(file)
                inputStream.use { input ->
                    outputStream.use { output ->
                        input?.copyTo(output)
                    }
                }
                bitmap = ImageRotationUtil.rotateAndCompressImage(file)
            }
        }
    )

    LaunchedEffect(location, bitmap) {
        if (location != null && bitmap != null) {
            recognizeText(bitmap!!, location!!, inputKm, context)
        }
    }

    Button(
        onClick = {
            if (permissions.allPermissionsGranted) {
                val locationHelper = LocationHelper(context)
                locationHelper.displayDistance {
                    location = it
                }
                uri = getImageUri(context)
                cameraLauncher.launch(uri)
            } else {
                permissions.launchMultiplePermissionRequest()
            }
        },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .requiredHeight(56.dp),
        enabled = b,
    ) { androidx.compose.material.Text("Tirar foto") }
}

private fun recognizeText(bitmap: Bitmap, location: Location, inputKm: String?, context: Context) {
    FirebaseVision.getInstance().onDeviceTextRecognizer.processImage(
        FirebaseVisionImage.fromBitmap(
            bitmap
        )
    )
        .addOnSuccessListener { firebaseVisionText ->
            processResultText(firebaseVisionText, location, bitmap, inputKm, context)
        }
        .addOnFailureListener { e ->
            Log.e("MainActivity", "Error: $e")
        }
}

private fun processResultText(
    resultText: FirebaseVisionText,
    location: Location,
    bitmap: Bitmap,
    inputKm: String?,
    context: Context
) {
    if (resultText.textBlocks.isEmpty()) return
    for (block in resultText.textBlocks) {
        for (line in block.lines) {
            val lineText = line.text
            Log.d("KILO", lineText)
        }
    }
    if (resultText.textBlocks.flatMap { it.lines }
            .map { it.text.toIntOrNull() }
            .any { it == inputKm!!.toIntOrNull() }) {

        Toast.makeText(context, "Validado", Toast.LENGTH_SHORT).show()
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 75, byteArrayOutputStream)
//        val base64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
//        Log.d("KILO", base64)
        Log.d("KILO", resultText.text)
        Log.d("KILO", location.latitude.toString())
        Log.d("KILO", location.longitude.toString())
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inputkm(
    value: String,
    onChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
    )
}

