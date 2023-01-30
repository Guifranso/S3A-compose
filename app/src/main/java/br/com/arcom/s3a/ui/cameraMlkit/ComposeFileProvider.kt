package br.com.arcom.s3a.ui.cameraMlkit


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider.getUriForFile
import java.io.File


fun getImageUri(context: Context): Pair<File, Uri> {
    // 1
    val directory = File(context.cacheDir, "images")
    directory.mkdirs()
    // 2
    val file = File.createTempFile(
        "selected_image_",
        ".jpg",
        directory
    )
    Log.d("KILO", file.absolutePath)

    // 3
    val authority = context.packageName + ".fileprovider"
    // 4
    return Pair(
        file, getUriForFile(
            context,
            authority,
            file,
        )
    )
}
