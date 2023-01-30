package br.com.arcom.s3a.util


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider.getUriForFile
import java.io.File


fun getImageUri(context: Context): Pair<File, Uri> {
    val directory = File(context.cacheDir, "images")
    directory.mkdirs()

    val file = File.createTempFile(
        "selected_image_",
        ".jpg",
        directory
    )

    val authority = context.packageName + ".fileprovider"

    return Pair(
        file, getUriForFile(
            context,
            authority,
            file,
        )
    )
}
