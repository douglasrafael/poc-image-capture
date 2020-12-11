package br.com.getnet.pocimagecapture.example2

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import br.com.getnet.pocimagecapture.LOG_TAG
import br.com.getnet.pocimagecapture.R
import br.com.getnet.pocimagecapture.databinding.ActivityDocumentScannerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.zynksoftware.documentscanner.ScanActivity
import com.zynksoftware.documentscanner.model.DocumentScannerErrorModel
import com.zynksoftware.documentscanner.model.ScannerResults
import java.io.File

class DocumentScannerActivity : ScanActivity() {
    private lateinit var binding: ActivityDocumentScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_document_scanner)

        addFragmentContentLayout()
    }

    override fun onError(error: DocumentScannerErrorModel) {
        Log.d(LOG_TAG, "onError ${error.errorMessage}")
        Toast.makeText(this, error.errorMessage?.error, Toast.LENGTH_LONG).show()
    }

    override fun onSuccess(scannerResults: ScannerResults) {
        handlerSuccess(scannerResults)
    }

    override fun onClose() {
        Log.d(LOG_TAG, "onClose")
        finish()
    }

    private fun handlerSuccess(scannerResults: ScannerResults) {
        scannerResults.originalImageFile?.let {
            Log.d(LOG_TAG, "originalImageFile size ${it.sizeInMb}")
        }

        scannerResults.croppedImageFile?.let {
            Log.d(LOG_TAG, "croppedImageFile size ${it.sizeInMb}")
        }

        scannerResults.transformedImageFile?.let {
            Log.d(LOG_TAG, "transformedImageFile size ${it.sizeInMb}")
        }

        scannerResults.croppedImageFile?.let {
            setImage(it.toUri())
        }
    }

    private val File.size get() = if (!exists()) 0.0 else length().toDouble()
    private val File.sizeInKb get() = size / 1024
    private val File.sizeInMb get() = sizeInKb / 1024

    private fun setImage(uri: Uri) {
        Glide.with(this).load(uri)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.photoImageView)
    }
}