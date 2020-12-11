package br.com.getnet.pocimagecapture

import android.app.Application
import android.graphics.Bitmap
import com.zynksoftware.documentscanner.ui.DocumentScanner

class App : Application() {
    companion object {
        private const val FILE_SIZE = 1000000L
        private const val FILE_QUALITY = 80
        private val FILE_TYPE = Bitmap.CompressFormat.JPEG
    }

    override fun onCreate() {
        super.onCreate()

        val configuration = DocumentScanner.Configuration()
        configuration.imageQuality = FILE_QUALITY
        configuration.imageType = FILE_TYPE
        configuration.imageSize = FILE_SIZE
        DocumentScanner.init(this, configuration)
    }
}