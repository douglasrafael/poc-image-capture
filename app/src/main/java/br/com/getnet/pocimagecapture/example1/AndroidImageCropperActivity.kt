package br.com.getnet.pocimagecapture.example1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import br.com.getnet.pocimagecapture.BaseActivity
import br.com.getnet.pocimagecapture.LOG_TAG
import br.com.getnet.pocimagecapture.R
import br.com.getnet.pocimagecapture.databinding.ActivityAndroidImageCropperBinding
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class AndroidImageCropperActivity : BaseActivity() {
    private lateinit var binding: ActivityAndroidImageCropperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_android_image_cropper)

        when (havePermissions()) {
            true -> openCamera()
            false -> requestPermissions()
        }
    }

    override fun onRefusedPermissions() {
        Log.d(LOG_TAG, "onRefusedPermissions()")
        Toast.makeText(this, R.string.message_refused_permissions, Toast.LENGTH_LONG).show()
    }

    override fun onGrantedPermissions() {
        Log.d(LOG_TAG, "onGrantedPermissions()")
        openCamera()
    }

    private fun openCamera() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.OFF)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .setActivityTitle("Documento")
            .setCropMenuCropButtonTitle("OK")
            .setOutputCompressQuality(80)
            .setAllowRotation(true)
            .setAllowFlipping(false)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d(LOG_TAG, "CROP_IMAGE_ACTIVITY_REQUEST_CODE()")

            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                setImage(result.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, R.string.message_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .into(binding.photoImageView)
    }
}