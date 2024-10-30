package com.dicoding.picodiploma.mycamera

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.mycamera.databinding.ActivityResultBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var indonesianEnglishTranslator: com.google.mlkit.nl.translate.Translator // Declare translator as a class variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        val detectedText = intent.getStringExtra(EXTRA_RESULT)
        binding.resultText.text = detectedText

        binding.translateButton.setOnClickListener {
            if (!detectedText.isNullOrEmpty()) {
                binding.progressIndicator.visibility = View.VISIBLE
                translateText(detectedText)
            } else {
                showToast(getString(R.string.no_text_detected))
            }
        }

        // Initialize the translator once
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.INDONESIAN)
            .build()
        indonesianEnglishTranslator = Translation.getClient(options)
    }

    private fun translateText(detectedText: String) {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        indonesianEnglishTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                indonesianEnglishTranslator.translate(detectedText)
                    .addOnSuccessListener { translatedText ->
                        binding.translatedText.text = translatedText
                        binding.progressIndicator.visibility = View.GONE
                    }
                    .addOnFailureListener { exception ->
                        showToast(exception.message.toString())
                        Log.e("Translation Error", exception.message.toString())
                        binding.progressIndicator.visibility = View.GONE
                    }
            }
            .addOnFailureListener { exception ->
                showToast(getString(R.string.downloading_model_fail))
                Log.e("Model Download Error", exception.message.toString())
                binding.progressIndicator.visibility = View.GONE
            }

        // No need to add observer here
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up the translator
        indonesianEnglishTranslator.close()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}
