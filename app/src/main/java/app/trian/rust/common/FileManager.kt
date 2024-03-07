package app.trian.rust.common

import android.net.Uri
import androidx.core.net.toFile
import java.io.File


class FileManager{

    fun copyFile(uri: Uri){
        val target= File("")
        uri.toFile().let {
            it.copyTo(target)
        }
    }
}