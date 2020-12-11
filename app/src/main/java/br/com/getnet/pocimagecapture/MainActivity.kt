package br.com.getnet.pocimagecapture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import br.com.getnet.pocimagecapture.databinding.ActivityMainBinding
import br.com.getnet.pocimagecapture.example1.AndroidImageCropperActivity
import br.com.getnet.pocimagecapture.example2.DocumentScannerActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // events
        binding.button1.setOnClickListener {
            startActivity(Intent(this, AndroidImageCropperActivity::class.java))
        }

        binding.button2.setOnClickListener {
            startActivity(Intent(this, DocumentScannerActivity::class.java))
        }
    }
}