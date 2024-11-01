package com.dicoding.picodiploma.mycamera

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.mycamera.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the image URI and result data from intent extras
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)
        val resultText = intent.getStringExtra(EXTRA_RESULT) ?: "No results available"

        if (imageUriString != null) {
            val imageUri = Uri.parse(imageUriString)
            Log.d("Image URI", "showImage: $imageUri")
            binding.resultImage.setImageURI(imageUri)
        } else {
            Log.e("ResultActivity", "Image URI is null")
        }

        // Display the result text
        binding.resultTextView.text = resultText
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}
