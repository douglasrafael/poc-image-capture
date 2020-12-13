package br.com.getnet.pocimagecapture.example3

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import br.com.getnet.pocimagecapture.BaseActivity
import br.com.getnet.pocimagecapture.LOG_TAG
import br.com.getnet.pocimagecapture.R
import br.com.getnet.pocimagecapture.databinding.ActivityFileChooserBinding

class FileChooserActivity : BaseActivity() {
    private lateinit var binding: ActivityFileChooserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_file_chooser)

        when (havePermissions()) {
            true -> openFilePicker()
            false -> requestPermissions()
        }
    }

    override fun onRefusedPermissions() {
        Log.d(LOG_TAG, "onRefusedPermissions()")
        Toast.makeText(this, R.string.message_refused_permissions, Toast.LENGTH_LONG).show()
    }

    override fun onGrantedPermissions() {
        Log.d(LOG_TAG, "onGrantedPermissions()")
        openFilePicker()
    }

    private fun openFilePicker() {
        val supportedMimeTypes = arrayOf("application/pdf")

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                val filename = getFileName(uri)
                val size = Formatter.formatShortFileSize(this, getFileSize(uri))
                Log.d(LOG_TAG, "onActivityResult() | filename: $filename | size: $size")

                binding.filenameTextView.text = filename.plus("\nSize: ").plus(size)
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getFileName(uri: Uri): String {
        var fileName = ""

        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null
        )
        cursor?.let {
            if (it.count <= 0) {
                it.close()
                throw IllegalArgumentException("Can't obtain file name, cursor is empty")
            }
            it.moveToFirst()
            fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            cursor.close()
        }

        return fileName
    }

    @Throws(IllegalArgumentException::class)
    private fun getFileSize(uri: Uri): Long {
        var size = 0L

        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null
        )
        cursor?.let {
            if (it.count <= 0) {
                it.close()
                throw IllegalArgumentException("Can't obtain file size, cursor is empty")
            }
            it.moveToFirst()
            size = it.getLong(it.getColumnIndexOrThrow(OpenableColumns.SIZE))
            cursor.close()
        }

        return size
    }

    companion object {
        private const val FILE_PICKER_REQUEST_CODE = 1
    }
}